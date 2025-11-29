import {cartServiceApi} from "./api.ts";
import type {AddItemRequest, CartDto} from "../types/cartTypes.ts";

export async function getCart(): Promise<CartDto> {
    return (await cartServiceApi.get("")).data.data
}

export async function getCartCount() {
    return (await cartServiceApi.get("/count")).data.data;
}

export async function addItemToCart(item: AddItemRequest) {
    return (await cartServiceApi.post("/items", item)).data.data;
}

export async function updateItemQuantity() {
    return (await cartServiceApi.patch("")).data.data;
}

export async function removeItemFromCart() {
    return (await cartServiceApi.delete("")).data.data;
}

export async function mergeGuestCart() {
    return (await cartServiceApi.post("/merge")).data.data;
}