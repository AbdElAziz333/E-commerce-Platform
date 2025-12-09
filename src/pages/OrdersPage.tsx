import {useEffect, useState} from "react";
import type {OrderDto} from "../types/orderTypes.ts";
import {getAllOrders} from "../services/orderService.ts";
import {Link} from "react-router-dom";

export default function OrdersPage() {
    const [orders, setOrders] = useState<OrderDto[]>();

    useEffect(() => {
        async function fetchOrders() {
            try {
                setOrders(await getAllOrders());
            } catch (err) {
                console.error(`Error fetching orders: ${err}`);
            }
        }

        fetchOrders();
    }, [])

    return (
        <div>
            <h1>Your Orders</h1>
            <ul>
                {orders?.map(((order) => (
                    <li key={order.orderNumber}>
                        {/*<Link to={`/products/${order.prod}`}>{order.}</Link>*/}
                        <hr />
                        <p>{order.orderId}</p>
                        <p>{order.paymentMethod}</p>
                        <p>{order.paymentStatus}</p>
                        <p>{order.estimatedDeliveryDate}</p>
                    </li>
                )))}
            </ul>
        </div>
    );
}