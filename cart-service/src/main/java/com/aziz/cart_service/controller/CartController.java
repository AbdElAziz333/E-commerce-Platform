package com.aziz.cart_service.controller;

import com.aziz.cart_service.dto.AddItemRequest;
import com.aziz.cart_service.dto.CartDto;
import com.aziz.cart_service.dto.UpdateQuantityRequest;
import com.aziz.cart_service.service.CartService;
import com.aziz.cart_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService service;

    @GetMapping
    public ResponseEntity<ApiResponse<CartDto>> getCart(
            @RequestHeader("User-Id") Long userId,
            @RequestHeader("Session-Id") String sessionId
    ) {
        CartDto cart = service.getOrCreateCart(userId, sessionId);
        return ResponseEntity.ok(ApiResponse.success("Fetched the cart successfully", cart));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Integer>> getCartCount(
            @RequestHeader("User-Id") Long userId,
            @RequestHeader("Session-Id") String sessionId
    ) {
        int count = service.getItemCount(userId, sessionId);
        return ResponseEntity.ok(ApiResponse.success("Fetched cart count", count));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartDto>> addItemToCart(
            @RequestHeader("User-Id") Long userId,
            @RequestHeader("Session-Id") String sessionId,
            @RequestBody AddItemRequest addItemRequest
    ) {
        CartDto cart = service.addItem(userId, sessionId, addItemRequest);
        return ResponseEntity.ok(ApiResponse.success("Item added", cart));
    }

    @PatchMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> updateItemQuantity(
            @RequestHeader("User-Id") Long userId,
            @RequestHeader("Session-Id") String sessionId,
            @PathVariable String productId,
            @RequestBody UpdateQuantityRequest updateQuantityRequest
    ) {
        CartDto cart = service.updateQuantity(userId, sessionId, productId, updateQuantityRequest.getQuantity());
        return ResponseEntity.ok(ApiResponse.success("Quantity updated", cart));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> removeItemFromCart(
            @RequestHeader("User-Id") Long userId,
            @RequestHeader("Session-Id") String sessionId,
            @PathVariable String productId
    ) {
        CartDto cart = service.removeItem(userId, sessionId, productId);
        return ResponseEntity.ok(ApiResponse.success("Item removed", cart));
    }

    @PostMapping("/merge")
    public ResponseEntity<ApiResponse<CartDto>> mergeGuestCart(
            @RequestHeader("User-Id") Long userId,
            @RequestHeader("Session-Id") String sessionId
    ) {
        CartDto cart = service.mergeGuestCartIntoUserCart(userId, sessionId);
        return ResponseEntity.ok(ApiResponse.success("Merged carts", cart));
    }
}