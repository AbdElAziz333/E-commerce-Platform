import {orderServiceApi} from "./api.ts";
import type {OrderCreationRequest, OrderDto} from "../types/orderTypes.ts";

export async function getAllOrders(): Promise<OrderDto[]> {
    return (await orderServiceApi.get("")).data.data;
}

export async function getOrderById(orderId: string): Promise<OrderDto> {
    return (await orderServiceApi.get(`/${orderId}`)).data.data;
}

export async function createOrder(orderCreationRequest: OrderCreationRequest): Promise<OrderDto> {
    return (await orderServiceApi.post("", orderCreationRequest)).data.data;
}

export async function deleteOrder(orderId: string): Promise<void> {
    return (await orderServiceApi.delete(`/${orderId}`));
}