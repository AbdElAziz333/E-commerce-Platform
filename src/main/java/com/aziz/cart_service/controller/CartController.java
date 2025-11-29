package com.aziz.cart_service.controller;

import com.aziz.cart_service.dto.AddItemRequest;
import com.aziz.cart_service.dto.CartDto;
import com.aziz.cart_service.dto.UpdateQuantityRequest;
import com.aziz.cart_service.dto.UserContext;
import com.aziz.cart_service.service.CartService;
import com.aziz.cart_service.util.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService service;

    @GetMapping
    public ResponseEntity<ApiResponse<CartDto>> getCart(
            @RequestHeader("User-Id") Long userId,
            @RequestHeader("Session-Id") String sessionId,
            HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getHeader("User-Id"));
        System.out.println(request.getHeader("Session-Id"));
        CartDto cart = service.getOrCreateCart(userId, sessionId);
        return ResponseEntity.ok(ApiResponse.success("Fetched the cart successfully", cart));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Integer>> getCartCount(HttpServletRequest request, HttpServletResponse response) {
        UserContext userContext = extractUserContext(request, response);
        int count = service.getItemCount(userContext.getUserId(), userContext.getSessionId());
        return ResponseEntity.ok(ApiResponse.success("Fetched cart count", count));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartDto>> addItemToCart(@RequestHeader("User-Id") Long userId, HttpServletRequest request, HttpServletResponse response, @RequestBody AddItemRequest addItemRequest) {
        UserContext userContext = new UserContext();

        if (userId != null) {
            userContext.setUserId(userId);
            CartDto cart = service.addItem(userContext.getUserId(), null, addItemRequest);
            return ResponseEntity.ok(ApiResponse.success("Item added", cart));
        }

        userContext = extractUserContext(request, response);
        CartDto cart = service.addItem(userContext.getUserId(), userContext.getSessionId(), addItemRequest);
        return ResponseEntity.ok(ApiResponse.success("Item added", cart));
    }

    @PatchMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> updateItemQuantity(HttpServletRequest request, HttpServletResponse response, @PathVariable String productId, @RequestBody UpdateQuantityRequest updateQuantityRequest) {
        UserContext userContext = extractUserContext(request, response);
        CartDto cart = service.updateQuantity(userContext.getUserId(), userContext.getSessionId(), productId, updateQuantityRequest.getQuantity());
        return ResponseEntity.ok(ApiResponse.success("Quantity updated", cart));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> removeItemFromCart(HttpServletRequest request, HttpServletResponse response, @PathVariable String productId) {
        UserContext userContext = extractUserContext(request, response);
        CartDto cart = service.removeItem(userContext.getUserId(), userContext.getSessionId(), productId);
        return ResponseEntity.ok(ApiResponse.success("Item removed", cart));
    }

    @PostMapping("/merge")
    public ResponseEntity<ApiResponse<CartDto>> mergeGuestCart(HttpServletRequest request, HttpServletResponse response) {
        UserContext userContext = extractUserContext(request, response);
        CartDto cart = service.mergeGuestCartIntoUserCart(userContext.getUserId(), userContext.getSessionId());
        return ResponseEntity.ok(ApiResponse.success("Merged carts", cart));
    }

    private UserContext extractUserContext(HttpServletRequest request, HttpServletResponse response) {

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("sessionId".equals(cookie.getName())) {
                    return UserContext.builder().sessionId(cookie.getValue()).build();
                }
            }
        }

        System.out.println("No Info??");
        return null;
    }
}