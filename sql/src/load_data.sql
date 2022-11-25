COPY Users
FROM '/Users/rohanbehera/Desktop/CS166/new-project166/data/users.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE users_userID_seq RESTART 101;

COPY Store
FROM '/Users/rohanbehera/Desktop/CS166/new-project166/data/stores.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Product
FROM '/Users/rohanbehera/Desktop/CS166/new-project166/data/products.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Warehouse
FROM '/Users/rohanbehera/Desktop/CS166/new-project166/data/warehouse.csv'
WITH DELIMITER ',' CSV HEADER;

COPY Orders
FROM '/Users/rohanbehera/Desktop/CS166/new-project166/data/orders.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE orders_orderNumber_seq RESTART 501;


COPY ProductSupplyRequests
FROM '/Users/rohanbehera/Desktop/CS166/new-project166/data/productSupplyRequests.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE productsupplyrequests_requestNumber_seq RESTART 11;

COPY ProductUpdates
FROM '/Users/rohanbehera/Desktop/CS166/new-project166/data/productUpdates.csv'
WITH DELIMITER ',' CSV HEADER;
ALTER SEQUENCE productupdates_updateNumber_seq RESTART 51;
