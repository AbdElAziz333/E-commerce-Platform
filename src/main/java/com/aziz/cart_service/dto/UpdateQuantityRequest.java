package com.aziz.cart_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuantityRequest {
    private Integer quantity;
}