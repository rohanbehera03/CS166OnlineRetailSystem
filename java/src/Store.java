//package com.cs166final;

public class Store {
    private double latitude;
    private double longitude;
    private int storeID;
    String storeName;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Store() {

    }

    public Store(double latitude, double longitude, int storeID, String storeName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeID = storeID;
        this.storeName = storeName;        
    }

    @Override
    public String toString() {
        return "Store Name : " + this.storeName + " Latitude: " + this.latitude + " Longitude: " + this.longitude;
    }

    public static void main(String[] args) {}
}
