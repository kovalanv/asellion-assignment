CREATE TABLE IF NOT EXISTS product
(
 productId       BIGSERIAL NOT NULL PRIMARY KEY,
 name 		 TEXT NOT NULL,
 currentprice 		 NUMERIC (8, 2) NOT NULL,
 lastUpdate timestamp NULL
);