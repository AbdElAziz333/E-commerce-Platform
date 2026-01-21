package com.aziz.order_service.mapper;

import com.aziz.order_service.request.CreateOrderRequest;
import com.aziz.order_service.dto.OrderDto;
import com.aziz.order_service.request.CreateOrderItemRequest;
import com.aziz.order_service.dto.OrderItemDto;
import com.aziz.order_service.model.Order;
import com.aziz.order_service.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    public OrderDto orderToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderStatus(order.getOrderStatus())
                .shippingAmount(order.getShippingAmount())
                .totalAmount(order.getTotalAmount())
                .shippingAddressId(order.getShippingAddressId())
                .paymentStatus(order.getPaymentStatus())
                .paymentMethod(order.getPaymentMethod())
                .notes(order.getNotes())
                .items(order.getItems().stream().map(this::itemToDto).toList())
                .build();
    }

    public Order createRequestToOrder(CreateOrderRequest request) {
        Order order = Order.builder()
                .shippingAmount(request.getShippingAmount())
                .notes(request.getNotes())
                .shippingAddressId(request.getShippingAddressId())
                .paymentMethod(request.getPaymentMethod())
                .build();

        List<OrderItem> items = request.getItems().stream().map(itemReq -> {
            OrderItem item = createOrderItemRequestToOrderItem(itemReq);
            item.setOrder(order);
            return item;
        }).toList();

        order.setItems(items);
        return order;
    }

    public OrderItem createOrderItemRequestToOrderItem(CreateOrderItemRequest request) {
        return OrderItem.builder()
                .productNameSnapshot(request.getProductNameSnapshot())
                .skuSnapshot(request.getSkuSnapshot())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .build();
    }

    public OrderItemDto itemToDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .productNameSnapshot(orderItem.getProductNameSnapshot())
                .skuSnapshot(orderItem.getSkuSnapshot())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .totalPrice(orderItem.getTotalPrice())
                .build();
    }
}