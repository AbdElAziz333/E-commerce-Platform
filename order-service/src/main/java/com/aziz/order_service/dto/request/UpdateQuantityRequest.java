package com.aziz.order_service.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuantityRequest {
    private Integer quantity;
}