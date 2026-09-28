import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import type {ProductDto} from "../types/productTypes.ts";
import {getAuthenticatedUserProducts} from "../services/productService.ts";
import {authenticatedUserPageUrl} from "../routes/routes.tsx";

export default function AuthenticatedUserProductsPage() {
    const [products, setProducts] = useState<ProductDto[]>();

    useEffect(() => {
        async function fetchProductsByUserId() {
            try {
                const res = await getAuthenticatedUserProducts()
                setProducts(res);
            } catch (err) {
                console.error(`Error fetching authenticated user products: ${err}`)
            }

        }

        fetchProductsByUserId();
    }, []);

    return (
        <div>
            <h1>My Products</h1>
            <ul>
                {products?.map(((product) => (
                    <li key={product.productId}>
                        <Link to={`/product/${product.slug}`}>
                            {product.name} {product.shortDescription} {product.price}$
                        </Link>
                    </li>
                )))}
            </ul>
            <br />
            <Link to={authenticatedUserPageUrl}>My Page</Link>
        </div>
    )
}