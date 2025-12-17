package com.aziz.notification_service.kafka.events;

import com.aziz.notification_service.util.enums.PaymentMethod;
import com.aziz.notification_service.util.enums.PaymentStatus;
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