package com.aziz.notification_service.kafka;

import com.aziz.notification_service.dto.OrderItemDto;
import com.aziz.notification_service.events.OrderEvent;
import com.aziz.notification_service.events.UserEvent;
import com.aziz.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationService service;

    @KafkaListener(topics = "${kafka.user-events}")
    public void consumeUserEvents(UserEvent event) {
        String msg;

        switch (event.getType()) {
            case USER_OTP_VERIFICATION -> {
                msg = String.format("""
                    Halo, your OTP: %s
                    It will expire after some time, please don't share it with anyone.
                    """, event.getOtp());
                service.sendAndSaveNotification(event.getEmail(), "OTP Verification", msg);
            }
            case USER_REGISTERED -> {
                msg = String.format("""
                        Halo %s, thanks for verifying the OTP and using our services! <3
                        """, event.getFirstName());
                service.sendAndSaveNotification(event.getEmail(), "Welcome!", msg);
            }
        }
    }

    @KafkaListener(topics = "${kafka.order-events}")
    public void consumeOrderEvents(OrderEvent event) {
        switch (event.getType()) {
            case ORDER_CREATED -> {
                StringBuilder itemDetails = getStringBuilder(event);

                String msg = """
                    Order Confirmation
                    Order Number: %s
                    Status: %s
                    Estimated Delivery: %s
                    Items Ordered:
                    %s
                    
                    Shipping Amount: $%.2f
                    Total Amount: $%.2f
                    
                    Shipping Address Id: %s
                    Thank you for your order!
                    """
                        .formatted(
                                event.getOrderNumber(),
                                event.getOrderStatus(),
                                event.getEstimatedDeliveryDate(),
                                itemDetails.toString(),
                                event.getShippingAmount(),
                                event.getTotalAmount(),
                                event.getShippingAddressId()
                        );

                service.sendAndSaveNotification(event.getEmail(), "Order Created", msg);
            }
            case ORDER_SHIPPED -> {
                //...
            }
        }
    }

    private static StringBuilder getStringBuilder(OrderEvent event) {
        StringBuilder itemDetails = new StringBuilder();

        for (OrderItemDto item : event.getOrderItems()) {
            itemDetails.append(String.format("""
                    - Product: %s
                      Quantity: %s
                      Price: $%.2f
                    """,
                    item.getProductNameSnapshot(),
                    item.getQuantity(),
                    item.getUnitPrice()
            ));
        }
        return itemDetails;
    }
}