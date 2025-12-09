package com.aziz.order_service.dto;

import com.aziz.order_service.util.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderUpdateRequest {
    private UUID orderId;
    private OrderStatus orderStatus;
    private LocalDate estimatedDeliveryDate;
    private LocalDate deliveredAt;
}