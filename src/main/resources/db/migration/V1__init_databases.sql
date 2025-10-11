CREATE TYPE preferred_language AS ENUM ('ARABIC', 'ENGLISH', 'RUSSIAN');
CREATE TYPE city AS ENUM ('CAIRO', 'EL_BEHEIRA', 'ALEXANDRIA', 'TANTA');
CREATE TYPE role AS ENUM ('ROLE_USER', 'ROLE_VENDOR', 'ROLE_ADMIN');

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(13) NOT NULL,
    role role,
    created_at DATE NOT NULL,
    last_modified_at DATE NOT NULL
);

CREATE TABLE address (
    address_id SERIAL PRIMARY KEY,
    street_line1 VARCHAR(150) NOT NULL,
    street_line2 VARCHAR(150),
    city city,
    state VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    is_default_shipping BOOLEAN NOT NULL,
    created_at DATE NOT NULL,
    last_modified_at DATE NOT NULL,
    user_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id)
    REFERENCES users(user_id) ON DELETE CASCADE
);