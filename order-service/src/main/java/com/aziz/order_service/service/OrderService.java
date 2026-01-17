package com.aziz.order_service.service;

import com.aziz.order_service.client.UserFeignClient;
import com.aziz.order_service.dto.OrderCreationRequest;
import com.aziz.order_service.dto.OrderDto;
import com.aziz.order_service.dto.OrderItemCreationRequest;
import com.aziz.order_service.dto.OrderUpdateRequest;
import com.aziz.order_service.kafka.OrderPublisher;
import com.aziz.order_service.kafka.events.OrderCreationEvent;
import com.aziz.order_service.kafka.events.OrderPaymentEvent;
import com.aziz.order_service.mapper.OrderMapper;
import com.aziz.order_service.model.Order;
import com.aziz.order_service.repository.OrderRepository;
import com.aziz.order_service.util.enums.OrderStatus;
import com.aziz.order_service.util.enums.PaymentStatus;
import com.aziz.order_service.util.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderPublisher publisher;
    private final UserFeignClient feignClient;

    /**
     * returns the
     * @param userId current authenticated user's id
     * */
    @Transactional(readOnly = true)
    public Page<OrderDto> getOrders(Long userId, Pageable pageable) {
        checkAuthenticated(userId);

        log.debug("Fetching orders for user: {}", userId);

        Page<Order> page = repository.findByUserId(userId, pageable);

        log.info("Successfully fetched orders page {} for user: {}", page.getNumber(), userId);

        return page.map(mapper::orderToDto);
    }

    /**
     * @param userId current authenticated user
     * @param id order id
     * @return OrderDto object
     * @author Aziz
     * */
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long userId, UUID id) {
        checkAuthenticated(userId);

        log.debug("Fetching order: {} for user: {}", id, userId);

        Order order = getOrderForUser(id, userId);
        log.info("Successfully fetched order: {}, for user: {}", id, userId);
        return mapper.orderToDto(order);
    }

    @Transactional
    public OrderDto createOrder(Long userId, OrderCreationRequest request) {
        checkAuthenticated(userId);

        log.debug("Creating order for user: {}", userId);

        validateOrderAmounts(request);

        Order order = mapper.creationRequestToOrder(request);
        order.setUserId(userId);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setOrderNumber(generateRandomOrderNumber());

        if (order.getOrderItems() != null) {
            order.getOrderItems().forEach(item -> item.setOrder(order));
        }

        Order savedOrder = repository.save(order);

        String email = feignClient.getCurrentUserEmail(userId).getData();
        publisher.publish(
                new OrderCreationEvent(
                        email,
                        savedOrder.getOrderNumber(),
                        savedOrder.getOrderStatus(),
                        savedOrder.getShippingAmount(),
                        savedOrder.getTotalAmount(),
                        savedOrder.getShippingAddressId(),
                        savedOrder.getOrderItems().stream().map(mapper::orderItemToDto).toList()
                )
        );

        publisher.publish(
                new OrderPaymentEvent(
                        userId,
                        email,
                        savedOrder.getOrderId(),
                        savedOrder.getOrderNumber(),
                        savedOrder.getOrderStatus(),
                        savedOrder.getTotalAmount(),
                        savedOrder.getPaymentMethod(),
                        savedOrder.getPaymentStatus()
                )
        );

        return mapper.orderToDto(savedOrder);
    }

    @Transactional
    public OrderDto updateOrder(Long userId, OrderUpdateRequest request) {
        checkAuthenticated(userId);

        log.debug("Updating order with id: {} for user: {}", request.getOrderId(), userId);

        Order order = getOrderForUser(request.getOrderId(), userId);

        order.setOrderStatus(request.getOrderStatus());
        repository.save(order);
        log.info("Order with id {} for user: {}, successfully updated", order.getOrderId(), userId);
        return mapper.orderToDto(order);
    }

    public void deleteOrder(Long userId, UUID id) {
        checkAuthenticated(userId);

        log.debug("Deleting order with id: {} for user: {}", id, userId);

        Order order = getOrderForUser(id, userId);
        repository.delete(order);
        log.info("Order with id: {} for user: {} deleted successfully", id, userId);
    }

    private void validateOrderAmounts(OrderCreationRequest request) {
        BigDecimal itemsTotal = request.getOrderItems().stream()
                .map(OrderItemCreationRequest::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expectedTotal = itemsTotal.add(request.getShippingAmount());

        if (expectedTotal.compareTo(request.getTotalAmount()) != 0) {
            throw new BadRequestException("Total amount mismatch. Expected: " + expectedTotal);
        }
    }

    /**
     * Generates random order number starts with ORD-
     * @author Aziz
     * */
    private String generateRandomOrderNumber() {
        String prefix = "ORD-";
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return prefix + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Checks if user is logged in by checking user id cookie
     * @throws UnauthorizedException if user id cookie is not found
     * @author Aziz
     * */
    private void checkAuthenticated(Long userId) {
        if (userId == null) {
            throw new UnauthorizedException("Please login first");
        }
    }

    /**
     *
     * @param orderId the order's id
     * @param userId the current authenticated user
     * @return Order object for the order
     * @author Aziz
     * */
    private Order getOrderForUser(UUID orderId, Long userId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Cannot fetch order with id: {}, order not found", orderId);
                    return new NotFoundException("Order not found with id: " + orderId);
                });

        if (!order.getUserId().equals(userId)) {
            log.warn("Cannot fetch order with id: {}, your user id: {}, order's user id: {}, you are not allowed to access it", orderId, userId, order.getUserId());
            throw new OrderAccessDeniedException("You are not allowed to access order: " + orderId);
        }

        return order;
    }
}