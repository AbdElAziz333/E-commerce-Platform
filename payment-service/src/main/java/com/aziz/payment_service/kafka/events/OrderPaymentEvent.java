package com.aziz.payment_service.kafka.events;

import com.aziz.payment_service.util.enums.OrderStatus;
import com.aziz.payment_service.util.enums.PaymentMethod;
import com.aziz.payment_service.util.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentEvent {
    private Long userId;
    private String email;
    private UUID orderId;
    private String orderNumber;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
}