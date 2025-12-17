package com.aziz.order_service.mapper;

import com.aziz.order_service.dto.OrderCreationRequest;
import com.aziz.order_service.dto.OrderDto;
import com.aziz.order_service.dto.OrderItemCreationRequest;
import com.aziz.order_service.dto.OrderItemDto;
import com.aziz.order_service.model.Order;
import com.aziz.order_service.model.OrderItem;
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
                .estimatedDeliveryDate(order.getEstimatedDeliveryDate())
                .deliveredAt(order.getDeliveredAt())
                .paymentStatus(order.getPaymentStatus())
                .paymentMethod(order.getPaymentMethod())
                .notes(order.getNotes())
                .build();
    }

    public Order creationRequestToOrder(OrderCreationRequest request) {
        return Order.builder()
                .orderItems(request.getOrderItems().stream().map(this::orderItemCreationRequestToOrderItem).toList())
                .shippingAmount(request.getShippingAmount())
                .totalAmount(request.getTotalAmount())
                .notes(request.getNotes())
                .shippingAddressId(request.getShippingAddressId())
                .paymentMethod(request.getPaymentMethod())
                .build();
    }

    public OrderItem orderItemCreationRequestToOrderItem(OrderItemCreationRequest request) {
        return OrderItem.builder()
                .productNameSnapshot(request.getProductNameSnapshot())
                .quantity(request.getQuantity())
                .skuSnapshot(request.getSkuSnapshot())
                .totalPrice(request.getTotalPrice())
                .unitPrice(request.getUnitPrice())
                .build();
    }

//    public OrderEvent orderToEvent(Order order) {
//        //    private OrderEventType type;
//        //    private String email;
//        //    private String orderNumber;
//        //    private OrderStatus orderStatus;
//        //    private BigDecimal shippingAmount;
//        //    private BigDecimal totalAmount;
//        //    private Long shippingAddressId;
//        //    private LocalDate estimatedDeliveryDate;
//        //    private List<OrderItemDto> orderItems;
//        return OrderEvent.builder()
//                .orderNumber(order.getOrderNumber())
//                .orderStatus(order.getOrderStatus())
//                .shippingAmount(order.getShippingAmount())
//                .totalAmount(order.getTotalAmount())
//                .shippingAddressId(order.getShippingAddressId())
//                .estimatedDeliveryDate(order.getEstimatedDeliveryDate())
//                .orderItems(order.getOrderItems().stream().map(this::orderItemToDto).toList())
//                .build();
//    }

    public OrderItemDto orderItemToDto(OrderItem orderItem) {
        return OrderItemDto.builder()
//                .orderItemId(orderItem.getOrderItemId())
                .productNameSnapshot(orderItem.getProductNameSnapshot())
//                .skuSnapshot(orderItem.getSkuSnapshot())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
//                .taxAmount(orderItem.getTaxAmount())
                .totalPrice(orderItem.getTotalPrice())
                .order(this.orderToDto(orderItem.getOrder()))
                .build();
    }
}