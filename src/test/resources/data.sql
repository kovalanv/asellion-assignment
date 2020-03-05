CREATE TABLE IF NOT EXISTS product
(
 productId       BIGSERIAL NOT NULL PRIMARY KEY,
 name 		 TEXT NOT NULL,
 price 		 NUMERIC (8, 2) NOT NULL,
 lastUpdatedTime timestamp NULL
);