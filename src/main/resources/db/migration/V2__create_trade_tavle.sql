DROP TABLE IF EXISTS "trade";

CREATE TABLE "orders"
(
    trade_id UUID PRIMARY KEY,
    buyer_id UUID NOT NULL,
    seller_id UUID NOT NULL,
    ticker_name VARCHAR(5) NOT NULL,
    quantity INT NOT NULL,
    price BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    trade_status VARCHAR(30) NOT NULL
);
