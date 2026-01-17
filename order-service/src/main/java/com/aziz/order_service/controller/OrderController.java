package com.aziz.order_service.controller;

import com.aziz.order_service.dto.OrderCreationRequest;
import com.aziz.order_service.dto.OrderDto;
import com.aziz.order_service.dto.OrderUpdateRequest;
import com.aziz.order_service.service.OrderService;
import com.aziz.order_service.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ApiResponse<Page<OrderDto>>> getOrders(
            @RequestHeader("User-Id") Long userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "orders for user: " + userId,
                        service.getOrders(userId, pageable.withPage(100))
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(
            @RequestHeader("User-Id") Long userId,
            @PathVariable UUID id
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
            @Valid @RequestBody OrderCreationRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Successfully created order for user: " + userId,
                        service.createOrder(userId, request)
                )
        );
    }

    @PutMapping
    public ResponseEntity<ApiResponse<OrderDto>> updateOrder(
            @RequestHeader("User-Id") Long userId,
            @Valid @RequestBody OrderUpdateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Successfully updated order with id: " + request.getOrderId(), service.updateOrder(userId, request)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteOrder(
            @RequestHeader("User-Id") Long userId,
            @PathVariable UUID id
    ) {
        service.deleteOrder(userId, id);
        return ResponseEntity.ok(ApiResponse.success("Successfully deleted order with id: " + id, null));
    }
}