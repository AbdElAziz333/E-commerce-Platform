import {useEffect, useState} from "react";
import type {CartDto} from "../types/cartTypes.ts";
import {getCart} from "../services/cartService.ts";
import {Link} from "react-router-dom";
import {authenticatedUserPageUrl} from "../routes/routes.tsx";

export default function CartPage() {
    const [cart, setCart] = useState<CartDto | null>();

    useEffect(() => {
        async function fetchCart() {
            try {
                setCart(await getCart());
            } catch (err) {
                console.error(`Error fetching cart: ${err}`);
            }
        }

        fetchCart();
    }, []);

    return (
        <div>
            <p>Cart Id: {cart?.cartId}</p>

            <ul>
                {cart?.items.map(((item) => (
                    <li key={item.productId}>
                        <hr />

                        <Link to={`/product/${item.productSlug}`}>
                            <span>Product Name: {item.productNameSnapshot}</span>
                        </Link>

                        <p>Quantity: {item.quantity}</p>
                        <p>Unit Price: {item.unitPrice}</p>
                        <p>Total Price: {item.totalPrice}</p>
                    </li>
                )))}
            </ul>

            <Link to={authenticatedUserPageUrl}>My Page</Link>
        </div>
    )
}