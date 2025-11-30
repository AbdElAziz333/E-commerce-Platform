package com.aziz.order_service.service;

import com.aziz.order_service.dto.OrderCreationRequest;
import com.aziz.order_service.dto.OrderDto;
import com.aziz.order_service.dto.OrderUpdateRequest;
import com.aziz.order_service.mapper.OrderMapper;
import com.aziz.order_service.model.Order;
import com.aziz.order_service.repository.OrderRepository;
import com.aziz.order_service.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper mapper;

    public List<OrderDto> getAllOrders(Long userId) {
        return repository.findAllOrdersByUserId(userId).stream().map(mapper::orderToDto).toList();
    }

    public OrderDto getOrderById(UUID id) {
        return repository.findById(id)
                .map(mapper::orderToDto)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    public OrderDto createOrder(OrderCreationRequest request) {
        Order order = mapper.creationRequestToOrder(request);

        //should make an event to the payment service...

        repository.save(order);
        return mapper.orderToDto(order);
    }

    public OrderDto updateOrder(OrderUpdateRequest request) {
        Order order = repository.findById(request.getOrderId()).orElseThrow(() -> new NotFoundException("Order not found with id: " + request.getOrderId(), HttpStatus.NOT_FOUND));

        order.setOrderStatus(request.getOrderStatus());
        order.setEstimatedDeliveryDate(request.getEstimatedDeliveryDate());
        order.setDeliveredAt(request.getDeliveredAt());
        repository.save(order);
        return mapper.orderToDto(order);
    }

    public void deleteOrder(UUID id) {
        Order order = repository.findById(id).orElseThrow(() -> new NotFoundException("Order not found with id: " + id, HttpStatus.NOT_FOUND));
        repository.delete(order);
    }
}