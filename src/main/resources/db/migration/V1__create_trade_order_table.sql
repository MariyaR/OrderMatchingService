DROP TABLE IF EXISTS "trade_order";

CREATE TABLE "trade_order"
(
  order_id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  operation_type VARCHAR(5) NOT NULL,
  ticker_name VARCHAR(5) NOT NULL,
  quantity INT NOT NULL,
  price BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  order_status VARCHAR(30) NOT NULL
);
