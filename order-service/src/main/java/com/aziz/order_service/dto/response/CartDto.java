package com.aziz.order_service.dto.response;

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