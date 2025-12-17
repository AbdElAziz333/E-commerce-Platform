CREATE TABLE payment (
    payment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    method VARCHAR(30) NOT NULL CHECK (method IN ('VISA', 'VODAFONE_CASH')),
    currency VARCHAR(30) NOT NULL CHECK (currency IN ('EGP', 'USD', 'EUR', 'RUB')),
    status VARCHAR(30) NOT NULL CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED', 'CANCELLED')),
    provider_transaction_id VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
