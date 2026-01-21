package com.aziz.order_service.controller;

import com.aziz.order_service.request.CreateOrderRequest;
import com.aziz.order_service.dto.OrderDto;
import com.aziz.order_service.request.UpdateOrderRequest;
import com.aziz.order_service.service.OrderService;
import com.aziz.order_service.util.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderDto>>> getOrders(
            @RequestHeader("User-Id") Long userId,
            @RequestParam(defaultValue = "0") @Min(0) int page
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Orders fetched successfully",
                        service.getOrders(userId, page)
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
                        "Order fetched successfully",
                        service.getOrderById(userId, id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(
            @RequestHeader("User-Id") Long userId,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        "Order created successfully",
                        service.createOrder(userId, request)
                )
        );
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrder(
            @RequestHeader("User-Id") Long userId,
            @PathVariable UUID orderId,
            @Valid @RequestBody UpdateOrderRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success("Order updated successfully", service.updateOrder(userId, orderId, request)));
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(
            @RequestHeader("User-Id") Long userId,
            @PathVariable UUID orderId
    ) {
        service.deleteOrder(userId, orderId);
    }
}