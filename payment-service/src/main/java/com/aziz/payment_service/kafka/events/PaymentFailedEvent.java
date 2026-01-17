package com.aziz.payment_service.kafka.events;

import com.aziz.payment_service.util.enums.PaymentMethod;
import com.aziz.payment_service.util.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFailedEvent {
    private String email;
    private String orderNumber;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
}