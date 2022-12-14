/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */
//package com.cs166final;

//import com.cs166final.Store;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Retail {

   // reference to physical database connection.
   private Connection _connection = null;

   //setting private variables for getter functions
   private String _url;

   private String _usr;

   private String _password;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of Retail shop
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param user the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Retail(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         this._url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;

         System.out.println ("Connection URL: " + _url + "\n");

         this._usr = user;

         this._password = passwd;
         // obtain a physical connection
         this._connection = DriverManager.getConnection(_url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Retail

   //getter functions for java database connection
   public String getUrl() {
      return _url;
   }

   public String getUser() {
      return _usr;
   }

   public String getPassword() {
      return _password;
   }

   public Connection getConnection() {
      return _connection;
   }
   // Method to calculate euclidean distance between two latitude, longitude pairs. 
   public static double calculateDistance (double lat1, double long1, double lat2, double long2){
      double t1 = (lat1 - lat2) * (lat1 - lat2);
      double t2 = (long1 - long2) * (long1 - long2);
      return Math.sqrt(t1 + t2); 
   }
   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult


   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Retail.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      Retail esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Retail object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Retail (dbname, dbport, user, "");
         
         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. View Stores within 30 miles");
                System.out.println("2. View Product List");
                System.out.println("3. Place a Order");
                System.out.println("4. View 5 recent orders");

                //the following functionalities basically used by managers
                System.out.println("5. Update Product");
                System.out.println("6. View 5 recent Product Updates Info");
                System.out.println("7. View 5 Popular Items");
                System.out.println("8. View 5 Popular Customers");
                System.out.println("9. Place Product Supply Request to Warehouse");
                System.out.println("10. Admin Update Product");
                System.out.println("11. Admin Update Users");

                System.out.println(".........................");
                System.out.println("20. Log out");
                switch (readChoice()){
                   case 1: viewStores(esql); break;
                   case 2: viewProducts(esql); break;
                   case 3: placeOrder(esql); break;
                   case 4: viewRecentOrders(esql); break;
                   case 5: updateProduct(esql); break;
                   case 6: viewRecentUpdates(esql); break;
                   case 7: viewPopularProducts(esql); break;
                   case 8: viewPopularCustomers(esql); break;
                   case 9: placeProductSupplyRequests(esql); break;
                   case 10: updateProductAdmin(esql); break;
                   case 11: updateUserAdmin(esql); break;

                   case 20: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();
         System.out.print("\tEnter latitude: ");   
         String latitude = in.readLine();       //enter lat value between [0.0, 100.0]
         System.out.print("\tEnter longitude: ");  //enter long value between [0.0, 100.0]
         String longitude = in.readLine();
         
         String type="Customer";

			String query = String.format("INSERT INTO USERS (name, password, latitude, longitude, type) VALUES ('%s','%s', %s, %s,'%s')", name, password, latitude, longitude, type);

         esql.executeUpdate(query);
         System.out.println ("User successfully created!");
      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }//end CreateUser


  /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String user_name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();
         
         String query = String.format("SELECT * FROM USERS WHERE name = '%s' AND password = '%s'", user_name, password);

         int userNum = esql.executeQuery(query);
	 if (userNum > 0)
		return user_name;
         return null;
      }catch(Exception e){
         System.err.println(e.getMessage());
         return null;
      }
   }//end

   // browse stores
   public static void viewStores(Retail esql) { 
     try {
          System.out.println("\tEnter user latitude: ");  //enter lat value between [0.0, 100.0]
         double userLatitude = Double.parseDouble(in.readLine());       
         System.out.println("\tEnter user longitude: ");  //enter long value between [0.0, 100.0]
         double userLongitude = Double.parseDouble(in.readLine());
      /*
      Query for all the stores : SELECT * FROM Stores;
      Create a list
      for each store S 
         calculate distance between user location and store S
         if distance <= 30 {
            add to list
         }
      end for 
      return list
      */
         String storeQuery =  String.format("SELECT * FROM Store");
         Statement stmt = esql.getConnection().createStatement();

         // issues the query instruction
            ResultSet rs = stmt.executeQuery(storeQuery);
            List<Store> nearbyStores = new ArrayList<Store>();
            int ctr = 0;
            while (rs.next()) {
               Store s = new Store();
               s.setStoreName(rs.getString("name"));
               s.setLatitude(rs.getDouble("latitude"));
               s.setLongitude(rs.getDouble("longitude"));
               s.setStoreID(rs.getInt("storeID"));
               double distance = calculateDistance(userLatitude, s.getLatitude(), userLongitude, s.getLongitude());
               if (distance <= 30) {
                  nearbyStores.add(s);
                  ctr++;
               }
             }   
            System.out.println("Number of stores within 30 miles: " + ctr);

            if (nearbyStores.size() == 0) {
               System.out.println("No nearby stores within 30 miles.. " );
            } 
            else {
               for (Store s: nearbyStores) {
                  System.out.println(s);
               }  
            }

      } catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }

   // browse products
   public static void viewProducts(Retail esql) {
      try {
         System.out.println("\tEnter a storeID for store you want to view products from: ");
         int store_id = Integer.parseInt(in.readLine());   
         String query = String.format("SELECT ProductName, numberOfUnits, pricePerUnit FROM Product WHERE storeID = '%s'", store_id);
         
         int rowCount = esql.executeQueryAndPrintResult(query);
         System.out.println("Products list: " + rowCount);
      }
      catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

   //order products User can order any product from the store within 30 miles radius of his/her location. User will be asked to input storeID, productName, and numberofUnits. 
   // After placing the order, the order information needs to be inserted in the Orders table.
   public static void placeOrder(Retail esql) {
      try {
         System.out.print("\tEnter user ID: ");
         String userID = in.readLine();
         System.out.print("\tEnter store ID: ");
         String storeID = in.readLine();
         System.out.print("\tEnter product name: ");
         String productName = in.readLine();
         System.out.print("\tEnter number of units: ");
         String numberOfUnits = in.readLine();

         java.util.Date date = new java.util.Date();
         Timestamp timestamp = new Timestamp(date.getTime());

         String query1 = String.format("INSERT INTO Orders (customerID, storeID, productName, unitsOrdered, orderTime) VALUES ('%s', '%s', '%s', '%s', '%s')", userID, storeID, productName, numberOfUnits, timestamp);
         String query2 = String.format("UPDATE Product SET numberOfUnits = (numberOfUnits - '%s') WHERE storeID = '%s' AND productName = '%s'", numberOfUnits, storeID, productName);
         esql.executeUpdate(query1);
         esql.executeUpdate(query2);
         System.out.println("Order placed.");
      } 
      catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }

   //managers update product information given storeID, they can view last 5 recent updates of their store
   public static void updateProduct(Retail esql) {
      try {
         System.out.print("\tEnter manager ID: ");
         String managerID = in.readLine();
         System.out.print("\tEnter store ID: ");
         String storeID = in.readLine();
         System.out.print("\tEnter product: ");
         String product = in.readLine();
         System.out.print("\tEnter new number of units: ");
         String numberOfUnits = in.readLine();
         System.out.print("\tEnter new price per unit: ");
         String pricePerUnit = in.readLine();
         String query1 = String.format("UPDATE Product SET numberOfUnits = '%s', pricePerUnit = '%s' WHERE productName = '%s' AND '%s' IN (SELECT S.storeID FROM Store S, Users U WHERE U.userID = S.managerID AND U.userID = '%s')", numberOfUnits, pricePerUnit, product, storeID, managerID);
         String query2 = String.format("INSERT INTO productUpdates(managerID, storeID, productName, updatedOn) VALUES('%s', '%s', '%s', now())", managerID, storeID, product);
         esql.executeUpdate(query1);
         esql.executeUpdate(query2);
         System.out.println("Item updated.");
      } 
      catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }

   //Admins will be able update products
   public static void updateProductAdmin(Retail esql) {
      try {
         System.out.print("\tEnter admin ID: ");
         String adminID = in.readLine();
         System.out.print("\tEnter store ID: ");
         String storeID = in.readLine();
         System.out.print("\tEnter product: ");
         String product = in.readLine();
         System.out.print("\tEnter new number of units: ");
         String numberOfUnits = in.readLine();
         System.out.print("\tEnter new price per unit: ");
         String pricePerUnit = in.readLine();
         String query1 = String.format("UPDATE Product SET numberOfUnits = '%s', pricePerUnit = '%s' WHERE productName = '%s' AND storeID = '%s'", numberOfUnits, pricePerUnit, product, storeID);
         String query2 = String.format("INSERT INTO productUpdates(managerID, storeID, productName, updatedOn) VALUES('%s', '%s', '%s', now())", adminID, storeID, product);
         esql.executeUpdate(query1);
         esql.executeUpdate(query2);
         System.out.println("Item updated.");
      } 
      catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }


   public static void updateUserAdmin(Retail esql) {
      try {
            System.out.print("\tEnter user ID: ");
            String userID = in.readLine();
            System.out.print("\tEnter new name: ");
            String name = in.readLine();
            System.out.print("\tEnter new password: ");
            String password = in.readLine();
            System.out.print("\tEnter new latitude: ");
            String latitude = in.readLine();
            System.out.print("\tEnter new longitude: ");
            String longitude = in.readLine();
            System.out.print("\tEnter new user type: ");
            String type = in.readLine();
            String query = String.format("UPDATE Users SET name = '%s', password = '%s', latitude = '%s', longitude = '%s', type = '%s' WHERE userID = '%s'", name, password, latitude, longitude, type, userID);
            esql.executeUpdate(query);
            System.out.println("User updated.");
      } 
      catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }


   
   //browse orders list. customers can see last 5 recent orders
   public static void viewRecentOrders(Retail esql) {
      try {
      System.out.println("\tEnter userID: ");
      int user_id = Integer.parseInt(in.readLine());   
     
       String query = String.format("SELECT O.storeID, S.name, O.productName, O.unitsOrdered, O.orderTime FROM Orders O, Store S, Users U WHERE O.storeID = S.storeID AND U.userID = O.customerID AND O.orderNumber IN (SELECT O.orderNumber FROM Orders O, Store S, Users U WHERE customerID = '%s' GROUP BY O.orderNumber ORDER BY MAX(O.orderTime) desc LIMIT 5)", user_id);

      int rowCount = esql.executeQueryAndPrintResult(query);
      System.out.println("Order history: " + rowCount);

      }
      catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

   //manager can see all the orders information of the store(s) he/she manages.
   public static void viewRecentUpdates(Retail esql) {
      try {
         System.out.println("\tEnter managerID: ");
         int manager_id = Integer.parseInt(in.readLine());

         String query = String.format("SELECT O.orderNumber, U.name, O.storeID, O.productName, O.orderTime FROM Orders O, Store S, Users U WHERE S.managerID = '%s' AND O.storeID = S.storeID AND U.userID = O.customerID", manager_id);
         int rowCount = esql.executeQueryAndPrintResult(query);
         System.out.println("Order information: " + rowCount);
      }
      catch(Exception e) {
         System.err.println(e.getMessage());
      }
   } 

   //manager can see top 5 most popular products in his/her store
   public static void viewPopularProducts(Retail esql) {
      try {
         System.out.println("\tEnter managerID: ");
         int manager_id = Integer.parseInt(in.readLine());
         
         System.out.println("\tEnter storeID: ");
         int store_id =  Integer.parseInt(in.readLine());

         String query = String.format("SELECT O.productName, O.unitsOrdered FROM Orders O, Store S, Users U WHERE O.storeID = S.storeID AND U.userID =  O.customerID AND O.orderNumber IN (SELECT O.orderNumber FROM Orders O, Store S, Users U WHERE S.storeID = '%s' AND S.managerID = '%s' GROUP BY O.orderNumber ORDER BY MAX(O.unitsOrdered) desc LIMIT 5)", store_id, manager_id);
         int rowCount = esql.executeQueryAndPrintResult(query);
         System.out.println("Top 5 most popular products in Store: " + rowCount);
      }
      catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }

   //manager can view the top 5 customer's information who placed the most orders in his/her store
   public static void viewPopularCustomers(Retail esql) {
     try {
         System.out.println("\tEnter managerID: ");
         int manager_id = Integer.parseInt(in.readLine());
         
         System.out.println("\tEnter storeID: ");
         int store_id =  Integer.parseInt(in.readLine());

         String query = String.format("SELECT U.userID, U.name, U.latitude, U.longitude, O.unitsOrdered FROM Orders O, Store S, Users U WHERE O.storeID = S.storeID AND U.userID =  O.customerID AND O.orderNumber IN (SELECT O.orderNumber FROM Orders O, Store S, Users U WHERE S.storeID = '%s' AND S.managerID = '%s' GROUP BY O.orderNumber ORDER BY MAX(O.unitsOrdered) desc LIMIT 5)", store_id, manager_id);
         int rowCount = esql.executeQueryAndPrintResult(query);
         System.out.println("Top 5 customers: " + rowCount);
      }
      catch(Exception e) {
         System.err.println(e.getMessage());
      } 
   }

   //put supply requests function
   public static void placeProductSupplyRequests(Retail esql) {
      try {
         System.out.print("\tEnter manager ID: ");
         String managerID = in.readLine();
         System.out.print("\tEnter store ID: ");
         String storeID = in.readLine();
         System.out.print("\tEnter product name: ");
         String product = in.readLine();
         System.out.print("\tEnter new number of units: ");
         String numberOfUnits = in.readLine();
         System.out.print("Enter warehouse ID: ");
         String warehouseID = in.readLine();
         String query1 = String.format("INSERT INTO ProductSupplyRequests(managerID, warehouseID, storeID, productName, unitsRequested) VALUES('%s', '%s', '%s', '%s', '%s')", managerID, warehouseID, storeID, product, numberOfUnits);
         String query2 = String.format("UPDATE Product SET numberOfUnits = numberOfUnits + %s WHERE productName = '%s' AND storeID = '%s'", numberOfUnits, product, storeID);
         esql.executeUpdate(query1);
         esql.executeUpdate(query2);
         System.out.println("Product supply request placed.");
      } 
      catch(Exception e) {
         System.err.println(e.getMessage());
      }
   }



}//end Retail
