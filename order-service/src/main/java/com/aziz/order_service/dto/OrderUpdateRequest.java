package com.aziz.order_service.dto;

import com.aziz.order_service.util.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateRequest {
    @NotNull
    private UUID orderId;

    @NotNull
    private OrderStatus orderStatus;
}