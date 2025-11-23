import {useEffect, useState} from "react";
import type {ProductDto} from "../types/productTypes.ts";
import {getAllProducts} from "../services/productService.ts";
import {Link} from "react-router-dom";
import {authenticatedUserPageUrl, homePageUrl} from "../routes/routes.tsx";

export default function ProductsPage() {
    const [products, setProducts] = useState<ProductDto[]>()

    useEffect(() => {
        async function fetchAllProducts() {
            try {
                setProducts(await getAllProducts())
            } catch (err) {
                console.error("Error fetching all products: " + err);
            }
        }

        fetchAllProducts()
    }, []);

    return (
        <div>
            <h1>All Products</h1>
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
            <br />
            <Link to={homePageUrl}>Home Page</Link>
        </div>
    )
}