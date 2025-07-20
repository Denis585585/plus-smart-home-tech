DROP TABLE IF EXISTS shopping_cart, shopping_cart_items;

CREATE TABLE IF NOT EXISTS shopping_cart (
    shopping_cart_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL,
    UNIQUE(username),
    active BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS shopping_cart_items (
    product_id UUID NOT NULL,
    quantity BIGINT,
    cart_id UUID REFERENCES shopping_cart (shopping_cart_id) ON DELETE CASCADE
);