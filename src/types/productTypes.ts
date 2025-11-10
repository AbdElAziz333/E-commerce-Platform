export interface ProductDto {
    id: string;
    name: string;
    description: string;
    shortDescription: string;
    sku: string;
    slug: string;
    price: number;
    stockQuantity: number;
    variantAttributes: string[];
}

export interface ProductCreationRequest {
    name: string;
    description: string;
    shortDescription: string;
    sku: string;
    slug: string;
    price: number;
    stockQuantity: number;
}

export interface ProductUpdateRequest {
    id: string;
    name: string;
    description: string;
    shortDescription: string;
    sku: string;
    price: number;
    stockQuantity: number;
}