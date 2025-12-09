CREATE TABLE cart (
    cart_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id BIGINT,
    session_id VARCHAR(255),
    status TEXT NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
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
    added_at TIMESTAMP NOT NULL
);