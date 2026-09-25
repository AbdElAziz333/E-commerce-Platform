CREATE TYPE o_status AS ENUM ('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELED');
CREATE TYPE p_status AS ENUM ('PENDING', 'PAID', 'FAILED', 'REFUNDED');
CREATE TYPE p_method AS ENUM ('VISA', 'VODAFONE_CASH');

CREATE TABLE orders (
    id UUID PRIMARY KEY,
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
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id UUID NOT NULL,
    product_name_snapshot VARCHAR(30) NOT NULL,
    sku_snapshot VARCHAR(30) NOT NULL,
    quantity INT NOT NULL,
    unit_price NUMERIC(12, 2) NOT NULL,
    total_price NUMERIC(12, 2) NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id)
        REFERENCES orders(id) ON DELETE CASCADE
);

CREATE TABLE cart (
    cart_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id BIGINT,
    session_id VARCHAR(255),
    status TEXT NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cart_item (
    cart_item_id SERIAL PRIMARY KEY,
    cart_id UUID REFERENCES cart(cart_id) ON DELETE CASCADE,
    product_id TEXT NOT NULL,
    product_name_snapshot VARCHAR NOT NULL,
    product_slug VARCHAR NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price_snapshot NUMERIC(19, 4) NOT NULL,
    total_price NUMERIC(19, 4) NOT NULL,
    added_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id)
        REFERENCES cart(cart_id) ON DELETE CASCADE
);