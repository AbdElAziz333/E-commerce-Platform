package com.aziz.order_service.service;

import com.aziz.order_service.client.UserFeignClient;
import com.aziz.order_service.model.OrderItem;
import com.aziz.order_service.request.CreateOrderRequest;
import com.aziz.order_service.dto.OrderDto;
import com.aziz.order_service.request.UpdateOrderRequest;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderPublisher publisher;
    private final UserFeignClient feignClient;

    @Transactional(readOnly = true)
    public Page<OrderDto> getOrders(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 100, Sort.by("createdAt").ascending());
        return repository.findByUserId(userId, pageable).map(mapper::orderToDto);
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long userId, UUID orderId) {
        log.debug("Fetching order: {} for user: {}", orderId, userId);
        return mapper.orderToDto(getOrderForUser(userId, orderId));
    }

    @Transactional
    public OrderDto createOrder(Long userId, CreateOrderRequest request) {
        log.debug("Creating order for user: {}", userId);

        Order order = mapper.createRequestToOrder(request);

        initializeOrder(order, userId);
        calculateAmounts(order);

        Order savedOrder = repository.save(order);

        publishEventsAfterCommit(savedOrder);

        log.info("Order {} created successfully for user {}", savedOrder.getOrderNumber(), userId);
        return mapper.orderToDto(savedOrder);
    }

    @Transactional
    public OrderDto updateOrder(Long userId, UUID orderId, UpdateOrderRequest request) {
        Order order = getOrderForUser(userId, orderId);

        order.setOrderStatus(request.getOrderStatus());

        log.info("Order {} updated to status {}", order.getOrderNumber(), order.getOrderStatus());
        return mapper.orderToDto(order);
    }

    @Transactional
    public void deleteOrder(Long userId, UUID orderId) {
        repository.delete(getOrderForUser(userId, orderId));
        log.info("Order {} deleted for user {}", orderId, userId);
    }

    private void initializeOrder(Order order, Long userId) {
        order.getItems().forEach(item -> item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity().longValue()))));
        order.setTotalAmount(BigDecimal.valueOf(1));
        order.setUserId(userId);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setOrderNumber(generateOrderNumber());
    }

    private void calculateAmounts(Order order) {
        BigDecimal itemsTotal = order.getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(itemsTotal.add(order.getShippingAmount()));
    }

    private void publishEventsAfterCommit(Order order) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        String email = feignClient.getCurrentUserEmail(order.getUserId()).getData();

                        publisher.publish(
                                new OrderCreationEvent(
                                        email,
                                        order.getOrderNumber(),
                                        order.getOrderStatus(),
                                        order.getShippingAmount(),
                                        order.getTotalAmount(),
                                        order.getShippingAddressId(),
                                        order.getItems().stream().map(mapper::itemToDto).toList()
                                )
                        );

                        publisher.publish(
                                new OrderPaymentEvent(
                                        order.getUserId(),
                                        email,
                                        order.getId(),
                                        order.getOrderNumber(),
                                        order.getOrderStatus(),
                                        order.getTotalAmount(),
                                        order.getPaymentMethod(),
                                        order.getPaymentStatus()
                                )
                        );
                    }
                }
        );
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }

    private Order getOrderForUser(Long userId, UUID orderId) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found: " + orderId));

        if (!order.getUserId().equals(userId)) {
            throw new OrderAccessDeniedException("Access denied for order: " + orderId);
        }

        return order;
    }
}