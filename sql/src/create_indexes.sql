DROP INDEX IF EXISTS idx_Users_userID;
DROP INDEX IF EXISTS idx_Store_storeID;
DROP INDEX IF EXISTS idx_Product_storeID_productName;
DROP INDEX IF EXISTS idx_Warehouse_warehouseID;
DROP INDEX IF EXISTS idx_Orders_orderNumber;
DROP INDEX IF EXISTS idx_ProductSupplyRequests_requestNumber;
DROP INDEX IF EXISTS idx_ProductUpdates_updateNumber;

CREATE UNIQUE INDEX idx_Users_userID on Users(userID);
CREATE UNIQUE INDEX idx_Store_storeID on Store(storeID);
CREATE UNIQUE INDEX idx_Product_storeID_productName on Product(storeID, productName);
CREATE UNIQUE INDEX idx_Warehouse_warehouseID on Warehouse(warehouseID);
CREATE UNIQUE INDEX idx_Orders_orderNumber on Orders(orderNumber);
CREATE UNIQUE INDEX idx_ProductSupplyRequests_requestNumber on ProductSupplyRequests(requestNumber);
CREATE UNIQUE INDEX idx_ProductUpdates_updateNumber on ProductUpdates(updateNumber);
