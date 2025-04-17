DROP TABLE IF EXISTS "order";

CREATE TABLE "orders"
(
  order_id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  operation_type VARCHAR(5) NOT NULL,
  ticker_name VARCHAR(5) NOT NULL,
  quantity INT NOT NULL,
  price DECIMAL NOT NULL,
  created_at TIMESTAMP NOT NULL
);
