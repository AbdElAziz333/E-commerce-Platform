import type {OrderItemDto} from "../types/orderTypes.ts";
import {useState} from "react";

export default function OrderCreationPage() {
    const [orderItems, setOrderItems] = useState<OrderItemDto[]>();

    return (
        <div>
            <form>

                <button type="submit">Create Order</button>
            </form>
        </div>
    )
}