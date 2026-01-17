package com.aziz.order_service.dto;

import com.aziz.order_service.util.enums.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    @NotNull
    private UUID orderId;

    @NotNull
    private String orderNumber;

    @NotNull
    private OrderStatus orderStatus;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal shippingAmount;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal totalAmount;

    @NotNull
    private Long shippingAddressId;

    @NotNull
    private PaymentStatus paymentStatus;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotNull
    private String notes;

    @NotNull
    @Valid
    private List<OrderItemDto> orderItems;
}