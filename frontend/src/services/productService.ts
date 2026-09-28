import type {ProductDto, ProductCreationRequest, ProductUpdateRequest} from "../types/productTypes.ts";
import {productServiceApi} from "./api.ts";

export async function getAllProducts(): Promise<ProductDto[]> {
    return (await productServiceApi.get("")).data.data
}

export async function getProductBySlug(slug: string | undefined): Promise<ProductDto> {
    return (await productServiceApi.get(`/${slug}`)).data.data
}

export async function createProduct(data: ProductCreationRequest): Promise<ProductDto> {
    return (await productServiceApi.post("", data)).data.data
}

export async function updateProduct(data: ProductUpdateRequest): Promise<ProductDto> {
    return (await productServiceApi.patch("", data)).data.data
}

export async function deleteProductById(productId: string): Promise<void> {
    return (await productServiceApi.delete(`/${productId}`)).data.data
}

export async function searchProducts(q: string, page = 0): Promise<ProductDto[]> {
    return (await productServiceApi.get(`/search?q=${q}&page=${page}`)).data.data;
}

export async function getAuthenticatedUserProducts(): Promise<ProductDto[]> {
    return (await productServiceApi.get(`/u`)).data.data
}