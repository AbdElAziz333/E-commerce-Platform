CREATE TYPE o_status AS ENUM ('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELED');
CREATE TYPE p_status AS ENUM ('PENDING', 'PAID', 'FAILED', 'REFUNDED');
CREATE TYPE p_method AS ENUM ('VISA', 'VODAFONE_CASH');

CREATE TABLE orders (
    order_id UUID PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL,
    order_status o_status NOT NULL,
    shipping_amount NUMERIC(12, 2) NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    notes TEXT,
    payment_status p_status NOT NULL,
    payment_method p_method NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    shipping_address_id BIGINT NOT NULL
);

CREATE TABLE order_item (
    order_item_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id UUID NOT NULL,
    product_name_snapshot VARCHAR(30) NOT NULL,
    sku_snapshot VARCHAR(30) NOT NULL,
    quantity INT NOT NULL,
    unit_price NUMERIC(12, 2) NOT NULL,
    total_price NUMERIC(12, 2) NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id)
        REFERENCES orders(order_id) ON DELETE CASCADE
);