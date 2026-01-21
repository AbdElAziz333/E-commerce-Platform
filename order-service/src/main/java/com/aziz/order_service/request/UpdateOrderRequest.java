package com.aziz.order_service.request;

import com.aziz.order_service.util.enums.OrderStatus;
import com.aziz.order_service.util.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest {
//    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

}