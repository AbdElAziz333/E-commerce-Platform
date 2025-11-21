import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import type {ProductDto} from "../types/productTypes.ts";
import {getProductBySlug} from "../services/productService.ts";

export default function ProductPage() {
    const {slug} = useParams();

    const [productData, setProductData] = useState<ProductDto>({
        userId: 0,
        productId: "",
        name: "",
        description: "",
        shortDescription: "",
        sku: "",
        slug: "",
        price: 0,
        stockQuantity: 0,
        variantAttributes: []
    })

    useEffect(() => {
        async function fetchProductData() {
            try {
                setProductData(await getProductBySlug(slug))
            } catch (err) {
                console.error(`Error fetching product: ${err}`)
            }
        }

        fetchProductData()
    }, [slug]);

    return (
        <div>
            <h1>Product Info</h1>
            <h3>creator: {productData.userId}</h3>
            <h3>Product Id: {productData.productId}</h3>
            <h3>Product Name: {productData.name}</h3>
            <h3>Product Description: {productData.description}</h3>
            <h3>Product Short Description: {productData.shortDescription}</h3>
            <h3>Product Sku: {productData.sku}</h3>
            <h3>Product Price: {productData.price}</h3>
            <h3>Product Stock: {productData.stockQuantity}</h3>
            <h3>Product Variants: {productData.variantAttributes}</h3>
        </div>
    )
}