import type {Carrier, OrderStatus, PaymentMethod, PaymentStatus} from "./commonTypes.ts";

export interface Order {
    orderNumber: string;
    orderStatus: OrderStatus;
    totalAmount: number;
    carrier: Carrier;
    trackingNumber: string;
    estimatedDeliveryDate: Date
    deliveredAt: Date
    notes: String
}

export interface OrderDto {

}

export interface OrderCreationRequest {
    productIds: number[]
    shippingAmount: number
    totalAmount: number
    notes: string
    paymentMethod: PaymentMethod
}

export interface OrderUpdateRequest {
    orderStatus: OrderStatus
    paymentStatus: PaymentStatus
    estimatedDeliveryDate: Date
    deliveredAt: Date
}

export interface OrderItem {
    orderId: string
    productId: string
    productNameSnapshot: string
    skuSnapshot: string
    quantity: number
    unitPrice: number
    taxAmount: number
    totalPrice: number
    variantAttributes: string[]
    returnedQuantity: number
}