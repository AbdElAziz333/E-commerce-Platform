package com.aziz.cart_service.mapper;

import com.aziz.cart_service.dto.AddItemRequest;
import com.aziz.cart_service.dto.CartDto;
import com.aziz.cart_service.dto.CartItemDto;
import com.aziz.cart_service.model.Cart;
import com.aziz.cart_service.model.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartMapper {
    public CartDto cartToDto(Cart cart) {
        return CartDto.builder()
                .cartId(cart.getCartId())
                .sessionId(cart.getSessionId())
                .userId(cart.getUserId())
                .items(cart.getItems().stream().map(this::cartItemToDto).toList())
                .build();
    }

    public CartItemDto cartItemToDto(CartItem item) {
        return CartItemDto.builder()
                .productId(item.getProductId())
                .productNameSnapshot(item.getProductNameSnapshot())
                .productSlug(item.getProductSlug())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPriceSnapshot())
                .totalPrice(item.getTotalPrice())
                .build();
    }

    public CartItem addRequestToCartItem(AddItemRequest request, Cart cart) {
        return CartItem.builder()
                .productId(request.getProductId())
                .productNameSnapshot(request.getProductNameSnapshot())
                .productSlug(request.getProductSlug())
                .quantity(request.getQuantity())
                .unitPriceSnapshot(request.getUnitPrice())
                .totalPrice(request.getUnitPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                .cart(cart)
                .build();
    }
}