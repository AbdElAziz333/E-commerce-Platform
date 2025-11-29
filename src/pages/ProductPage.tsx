import {Link, useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import type {ProductDto} from "../types/productTypes.ts";
import {getProductBySlug} from "../services/productService.ts";
import {addItemToCart} from "../services/cartService.ts";
import type {AddItemRequest} from "../types/cartTypes.ts";
import {authenticatedUserPageUrl, cartPageUrl} from "../routes/routes.tsx";

export default function ProductPage() {
    const {slug} = useParams();
    const navigate = useNavigate();

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

    const [quantity, setQuantity] = useState<number>(1);

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

    async function handleAddToCart(e: React.FormEvent) {
        e.preventDefault();

        if (!productData) {
            return;
        }

        const addItemRequest: AddItemRequest = {
            productId: productData.productId,
            productSlug: productData.slug,
            quantity: quantity,
            unitPrice: productData.price,
            productNameSnapshot: productData.name
        };

        try {
            await addItemToCart(addItemRequest);
            navigate(cartPageUrl);
            console.log(`${productData.name} add successfully to cart!`)
        } catch (err) {
            console.error(`Error adding item to cart: ${err}`);
        }
    }

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

            <form onSubmit={handleAddToCart}>
                <p>Quantity</p>
                <input type="number" value={quantity} onChange={e => setQuantity(Number(e.target.value))} min="1" required />

                <button type="submit">Add item to cart</button>
            </form>

            <Link to={authenticatedUserPageUrl}>My Page</Link>
        </div>
    )
}