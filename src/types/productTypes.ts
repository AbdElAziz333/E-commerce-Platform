export interface ProductDto {
    userId: number
    productId: string
    name: string
    description: string
    shortDescription: string
    sku: string
    slug: string
    price: number
    stockQuantity: number
    variantAttributes: string[]
}

export interface ProductCreationRequest {
    userId: number
    name: string
    description: string
    shortDescription: string
    sku: string
    price: number
    stockQuantity: number
    variantAttributes: string[]
}

export interface ProductUpdateRequest {
    productId: string
    userId: number
    name: string
    description: string
    shortDescription: string
    sku: string
    price: number
    stockQuantity: number
    variantAttributes: string[]
}