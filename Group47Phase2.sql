CREATE SCHEMA IF NOT EXISTS ONLINE_SHOPPING;


DROP TABLE IF EXISTS Users, Customer, Manager, Store, Product, Warehouse, Orders, Supplies, Requests, Updates, Manages;

CREATE TABLE ONLINE_SHOPPING.Users (
	userID INT NOT NULL,
	password CHAR(11) NOT NULL,
	name CHAR(50) NOT NULL,
	email CHAR(50),
	PRIMARY KEY(userID)
);

CREATE TABLE ONLINE_SHOPPING.Customer (
	customerID INT NOT NULL,
	userID INT NOT NULL,
	creditScore INT, 
	latitude DECIMAL(8,6) NOT NULL, 
	longitude DECIMAL(9,6) NOT NULL,
	PRIMARY KEY(customerID),
	FOREIGN KEY(userID) REFERENCES ONLINE_SHOPPING.Users
);

CREATE TABLE ONLINE_SHOPPING.Manager (
	managerID INT NOT NULL,
	userID INT NOT NULL,
	degree CHAR(20), 
	salary INT NOT NULL,
	PRIMARY KEY(managerID),
	FOREIGN KEY(userID) REFERENCES Users
);

CREATE TABLE ONLINE_SHOPPING.Store (
	storeID INT NOT NULL,
	name CHAR(30) NOT NULL, 
	latitude DECIMAL(8,6) NOT NULL, 
	longitude DECIMAL(9,6) NOT NULL, 
	dateEstablished DATE,
	PRIMARY KEY(storeID)
);

CREATE TABLE ONLINE_SHOPPING.Product (
	productID INT NOT NULL,
	storeID INT NOT NULL,
	productName CHAR(30) NOT NULL,
	numberOfUnits INT NOT NULL,
	pricePerUnit INT NOT NULL,
	description CHAR(100),
	imageURL CHAR(30),
	PRIMARY KEY(productID),
	FOREIGN KEY(storeID) REFERENCES Store
);

CREATE TABLE ONLINE_SHOPPING.Warehouse (
	warehouseID INT NOT NULL, 
	area INT, 
	latitude DECIMAL(8,6) NOT NULL, 
	longitude DECIMAL(9,6) NOT NULL,
	PRIMARY KEY(warehouseID)
);


---Relationships---
CREATE TABLE ONLINE_SHOPPING.Orders (
	orderID INT NOT NULL,
	unitsOrdered INT NOT NULL,
	customerID INT NOT NULL,
	orderDate DATE NOT NULL,
	productID INT NOT NULL,
	PRIMARY KEY(orderID),
	FOREIGN KEY(customerID) REFERENCES Customer,
	FOREIGN KEY(productID) REFERENCES Product
);

CREATE TABLE ONLINE_SHOPPING.Supplies (
	supplyID INT NOT NULL,
	warehouseID INT NOT NULL,
	productID INT NOT NULL,
	PRIMARY KEY(warehouseID, productID),
	FOREIGN KEY(warehouseID) REFERENCES Warehouse,
	FOREIGN KEY(productID) REFERENCES Product
);

CREATE TABLE ONLINE_SHOPPING.Requests (
	requestID SERIAL NOT NULL,
	unitsRequested INT NOT NULL,
	warehouseID INT NOT NULL,
	productID INT NOT NULL,
	managerID INT NOT NULL,
    PRIMARY KEY(managerID, warehouseID),
    FOREIGN KEY(managerID) REFERENCES Manager(managerID),
   FOREIGN KEY(warehouseID) REFERENCES Warehouse(warehouseID),
	FOREIGN KEY(productID) REFERENCES Product(productID)
);

CREATE TABLE ONLINE_SHOPPING.Updates (
	updateID serial NOT NULL,
	managerID INT NOT NULL,
	productID INT NOT NULL,
    PRIMARY KEY(managerID, productID),
    FOREIGN KEY(managerID) REFERENCES Manager,
    FOREIGN KEY(productID) REFERENCES Product
);




