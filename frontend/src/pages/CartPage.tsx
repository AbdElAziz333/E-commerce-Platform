import {useEffect, useState} from "react";
import type {CartDto} from "../types/cartTypes.ts";
import {getCart} from "../services/cartService.ts";
import {Link, useNavigate} from "react-router-dom";
import {authenticatedUserPageUrl} from "../routes/routes.tsx";
import {createOrder} from "../services/orderService.ts";
import OrderCreationPage from "./OrderCreationPage.tsx";
import type {OrderCreationRequest, OrderItemDto, PaymentMethod} from "../types/orderTypes.ts";

export default function CartPage() {
    const [cart, setCart] = useState<CartDto | null>();
    const [orderCreationRequest, setOrderCreationRequest] = useState<OrderCreationRequest>();
    const [selectedItems, setSelectedItems] = useState<number[]>([]);
    const navigate = useNavigate();

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

    function handleCheckout(e: React.FormEvent<HTMLInputElement>) {
        e.preventDefault();

        try {
            createOrder(or)
        }
    }

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
                        <input
                            type="checkbox"
                            checked={selectedItems.includes(item.productId)}
                            onChange={() => {
                                setSelectedItems(prev =>
                                    prev.includes(item.productId)
                                        ? prev.filter(id => id !== item.productId) // remove if already selected
                                        : [...prev, item.productId] // add if not
                                );
                            }}
                        />
                    </li>
                )))}
            </ul>

            {/*here for every element above is checked with radio it should be sent */}
            <button onClick={handleCheckout}>Checkout</button>

            <Link to={authenticatedUserPageUrl}>My Page</Link>
        </div>
    )
}