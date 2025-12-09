CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE orders (
    order_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_number VARCHAR(50) NOT NULL,
    order_status VARCHAR(30) NOT NULL CHECK (order_status IN ('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELED')),
    shipping_amount NUMERIC NOT NULL,
    total_amount NUMERIC NOT NULL,
--    carrier VARCHAR(30) NOT NULL CHECK (carrier IN ('FedEx', 'Aramex')),
    tracking_number VARCHAR(50),
    estimated_delivery_date DATE,
    delivered_at DATE,
    notes TEXT,
    payment_status VARCHAR(20) NOT NULL CHECK (payment_status IN ('PENDING', 'PAID', 'REFUNDED')),
    payment_method VARCHAR(20) NOT NULL CHECK (payment_method IN ('VISA', 'VODAFONE_CASH', 'PAYPAL')),
    created_at DATE NOT NULL,
    updated_at DATE NOT NULL,
    user_id BIGINT NOT NULL,
    shipping_address_id BIGINT NOT NULL
--    transaction_id BIGINT NOT NULL
);

CREATE TABLE order_item (
    order_item_id SERIAL PRIMARY KEY,
    product_name_snapshot VARCHAR(30) NOT NULL,
    sku_snapshot VARCHAR(30) NOT NULL,
    quantity INT NOT NULL,
    unit_price NUMERIC NOT NULL,
--    tax_amount NUMERIC NOT NULL,
    total_price NUMERIC NOT NULL,
--    variant_attributes JSONB NOT NULL,
    order_id UUID NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);