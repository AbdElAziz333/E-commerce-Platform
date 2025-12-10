package com.aziz.order_service.service;

import com.aziz.order_service.client.UserFeignClient;
import com.aziz.order_service.dto.OrderCreationRequest;
import com.aziz.order_service.dto.OrderDto;
import com.aziz.order_service.dto.OrderItemCreationRequest;
import com.aziz.order_service.dto.OrderUpdateRequest;
import com.aziz.order_service.kafka.OrderPublisher;
import com.aziz.order_service.mapper.OrderMapper;
import com.aziz.order_service.model.Order;
import com.aziz.order_service.repository.OrderRepository;
import com.aziz.order_service.util.enums.OrderStatus;
import com.aziz.order_service.util.enums.PaymentStatus;
import com.aziz.order_service.util.exception.BadRequestException;
import com.aziz.order_service.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderPublisher publisher;
    private final UserFeignClient feignClient;
//    private final KafkaConfig kafkaConfig;
//    private final KafkaTemplate<Long, OrderCreationEvent> kafkaTemplate;

    /**
     * TODO: needs pagination
     *
     * @param userId current authenticated user id
     * */
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders(Long userId) {
        log.debug("Fetching orders for user with id: {}", userId);
        List<OrderDto> orders = repository.findAllOrdersByUserId(userId).stream().map(mapper::orderToDto).toList();
        log.info("Fetched {} order", orders.size());
        return orders;
    }

    /**
     * @param userId current authenticated user
     * @param id order id
     * @return OrderDto object
     * @author Aziz
     * */
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long userId, UUID id) {
        log.debug("Fetching order with id: {} for user: {}", id, userId);

        return repository.findById(id)
                .map(mapper::orderToDto)
                .orElseThrow(() -> {
                    log.warn("Order not found with id: {}", id);
                    return new NotFoundException("Order not found with id: " + id);
                });
    }


    @Transactional
    public OrderDto createOrder(Long userId, OrderCreationRequest request) {
        log.debug("Creating order for user with id: {}", userId);

        validateOrderItems(request.getOrderItems());
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
        publisher.publishOrder(
                email,
                savedOrder.getOrderNumber(),
                savedOrder.getOrderStatus(),
                savedOrder.getShippingAmount(),
                savedOrder.getTotalAmount(),
                savedOrder.getShippingAddressId(),
                savedOrder.getEstimatedDeliveryDate(),
                savedOrder.getOrderItems().stream().map(mapper::orderItemToDto).toList());

        return mapper.orderToDto(savedOrder);
    }

    @Transactional
    public OrderDto updateOrder(Long userId, OrderUpdateRequest request) {
        log.debug("Updating order with id: {} for user: {}", request.getOrderId(), userId);

        Order order = repository.findById(request.getOrderId())
                .orElseThrow(() -> {
                    log.warn("Cannot update order with id: {}, order not found", request.getOrderId());
                    return new NotFoundException("Order not found with id: " + request.getOrderId());
                });

        order.setOrderStatus(request.getOrderStatus());
        order.setEstimatedDeliveryDate(request.getEstimatedDeliveryDate());
        order.setDeliveredAt(request.getDeliveredAt());
        repository.save(order);
        log.info("Order with id {} for user: {}, successfully updated", order.getOrderId(), userId);
        return mapper.orderToDto(order);
    }

    public void deleteOrder(Long userId, UUID id) {
        log.debug("Deleting order with id: {} for user: {}", id, userId);
        Order order = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot delete order with id: {}, order not found", id);
                    return new NotFoundException("Order not found with id: " + id);
                });
        repository.delete(order);
        log.info("Order with id: {} for user: {} deleted successfully", id, userId);
    }

    private void validateOrderItems(List<OrderItemCreationRequest> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new BadRequestException("Order must contain at least one item");
        }

        for (OrderItemCreationRequest item : orderItems) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new BadRequestException("Order item quantity must be greater than 0");
            }

            if (item.getUnitPrice() == null || item.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("Order item unit price must be greater than 0");
            }

            if (item.getTotalPrice() == null || item.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("Order item total price must be greater than 0");
            }
        }
    }

    private void validateOrderAmounts(OrderCreationRequest request) {

    }

    private String generateRandomOrderNumber() {
        String prefix = "ORD-";
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return prefix + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}