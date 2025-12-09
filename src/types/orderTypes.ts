export type OrderStatus = "PENDING" | "CONFIRMED" | "SHIPPED" | "DELIVERED" | "CANCELED"
export type Carrier = "FedEx" | "Aramex"
export type PaymentMethod = "VISA" | "VODAFONE_CASH" | "PAYPAL"
export type PaymentStatus = "PENDING" | "PAID" | "FAILED" | "REFUNDED"

export interface OrderDto {
    orderId: string;
    orderNumber: string;
    orderStatus: OrderStatus;
    shippingAmount: number;
    totalAmount: number;
    shippingAddressId: number;
    carrier: string;
    trackingNumber: string;
    estimatedDeliveryDate: string;
    deliveredAt: string;
    notes: string[];
    transactionId: number;
    paymentStatus: PaymentStatus;
    paymentMethod: PaymentMethod;
    createdAt: Date;
    updatedAt: Date
}

export interface OrderItemDto {
    orderItemId: number;
    productNameSnapshot: string;
    skuSnapshot: string;
    quantity: number;
    unitPrice: number;
    taxAmount: number;
    totalPrice: number;
    // variantAttributes: string[]
    // returnedQuantity: number;
    order: OrderDto;
    productId: string;
}

export interface OrderCreationRequest {
    orderItems: OrderItemDto[];
    shippingAmount: number;
    totalAmount: number;
    notes: string
    userId: number;
    shippingAddressId: number;
    transactionId: number;
    paymentMethod: PaymentMethod;
}