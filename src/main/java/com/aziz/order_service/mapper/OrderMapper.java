package com.aziz.order_service.mapper;

import com.aziz.order_service.dto.OrderCreationRequest;
import com.aziz.order_service.dto.OrderDto;
import com.aziz.order_service.model.Order;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class OrderMapper {
    public OrderDto orderToDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .orderStatus(order.getOrderStatus())
                .shippingAmount(order.getShippingAmount())
                .totalAmount(order.getTotalAmount())
                .shippingAddressId(order.getShippingAddressId())
                .carrier(order.getCarrier())
                .trackingNumber(order.getTrackingNumber())
                .estimatedDeliveryDate(order.getEstimatedDeliveryDate())
                .deliveredAt(order.getDeliveredAt())
                .transactionId(order.getTransactionId())
                .paymentStatus(order.getPaymentStatus())
                .paymentMethod(order.getPaymentMethod())
                .notes(order.getNotes())
                .build();
    }

    public Order creationRequestToOrder(OrderCreationRequest request) {
        return Order.builder()
                .orderItems(request.getOrderItems())
                .shippingAmount(request.getShippingAmount())
                .totalAmount(request.getTotalAmount())
                .notes(request.getNotes())
                .userId(request.getUserId())
                .shippingAddressId(request.getShippingAddressId())
                .transactionId(request.getTransactionId())
                .paymentMethod(request.getPaymentMethod())
                .build();
    }
}