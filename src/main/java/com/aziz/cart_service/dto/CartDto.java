package com.aziz.cart_service.dto;

import com.aziz.cart_service.model.CartItem;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private UUID cartId;
    private String sessionId;
    private Long userId;
    private List<CartItemDto> items = new ArrayList<>();
}