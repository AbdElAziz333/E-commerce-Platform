package com.aziz.cart_service.service;

import com.aziz.cart_service.dto.AddItemRequest;
import com.aziz.cart_service.dto.CartDto;

public interface CartStorageStrategy {
    CartDto getOrCreateCart(Long userId, String sessionId);
    CartDto getCartIfExists(Long userId, String sessionId);
    int getItemCount(Long userId, String sessionId);
    CartDto addItem(Long userId, String sessionId, AddItemRequest request);
    CartDto updateQuantity(Long userId, String sessionId, String productId, int quantity);
    CartDto removeItem(Long userId, String sessionId, String productId);
    CartDto replaceCart(Long userId, CartDto newCart);
    void invalidate(String sessionId);
}