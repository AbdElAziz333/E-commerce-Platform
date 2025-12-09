package com.aziz.order_service.controller;

import com.aziz.order_service.dto.OrderCreationRequest;
import com.aziz.order_service.dto.OrderDto;
import com.aziz.order_service.dto.OrderUpdateRequest;
import com.aziz.order_service.service.OrderService;
import com.aziz.order_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders(
            @RequestHeader("User-Id") Long userId
    ) {
        return ResponseEntity.ok(ApiResponse.success("All orders", service.getAllOrders(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> getAllOrders(
            @RequestHeader("User-Id") Long userId,
            UUID id
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Successfully fetched order with id: " + id,
                        service.getOrderById(userId, id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(
            @RequestHeader("User-Id") Long userId,
            @RequestBody OrderCreationRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Successfully created order", service.createOrder(userId, request)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<OrderDto>> updateOrder(
            @RequestHeader("User-Id") Long userId,
            @RequestBody OrderUpdateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Successfully updated order with id: " + request.getOrderId(), service.updateOrder(userId, request)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteOrder(
            @RequestHeader("User-Id") Long userId,
            UUID id
    ) {
        service.deleteOrder(userId, id);
        return ResponseEntity.ok(ApiResponse.success("Successfully deleted order with id: " + id, null));
    }
}