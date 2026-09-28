// export interface Cart {
//     userId: number;
//     items: CartItemDto[];
// }

export interface CartItemDto {
    productId: string;
    productNameSnapshot: string;
    productSlug: string
    quantity: number;
    unitPrice: number;
    totalPrice: number;
}

export interface CartDto {
    cartId: string;
    sessionId: string;
    userId: number;
    items: CartItemDto[];
}

export interface AddItemRequest {
    productId: string;
    productSlug: string
    quantity: number;
    unitPrice: number;
    productNameSnapshot: string;
}