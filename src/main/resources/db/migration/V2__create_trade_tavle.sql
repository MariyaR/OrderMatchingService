DROP TABLE IF EXISTS "trade";

CREATE TABLE "trade"
(
    trade_id UUID PRIMARY KEY,
    buyer_id UUID NOT NULL,
    seller_id UUID NOT NULL,
    buy_order_id UUID NOT NULL,
    sell_order_id UUID NOT NULL,
    ticker_name VARCHAR(5) NOT NULL,
    quantity BIGINT NOT NULL,
    price NUMERIC(19, 4) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    trade_status VARCHAR(30) NOT NULL,
    failure_reason VARCHAR(30) NOT NULL
);
