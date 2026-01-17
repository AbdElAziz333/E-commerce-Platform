CREATE TYPE p_method AS ENUM ('VISA', 'VODAFONE_CASH');
CREATE TYPE p_currency AS ENUM ('EGP', 'USD', 'EUR', 'RUB');
CREATE TYPE p_status AS ENUM ('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED', 'CANCELLED');

CREATE TABLE payment (
    payment_id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    order_number VARCHAR(100) NOT NULL,
    user_id BIGINT NOT NULL,
    totalAmount NUMERIC(12,2) NOT NULL,
    method p_method NOT NULL,
    currency p_currency NOT NULL,
    status p_status NOT NULL DEFAULT 'PENDING',
    provider_transaction_id VARCHAR(150) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);