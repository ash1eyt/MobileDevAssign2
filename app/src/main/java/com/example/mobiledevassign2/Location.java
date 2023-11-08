package com.example.mobiledevassign2;

public class Location {

    private int id;         //Defines variables
    private String address;
    private double latitude;
    private double longitude;

    /*Constructor with paramters for id, address, latitude, longitude*/
   public Location(int id, String address, double latitude, double longitude){
       this.id = id;
       this.address = address;
       this.latitude = latitude;
       this.longitude = longitude;
   }
    /*Constructor with parameters for address, latitude, longitude*/
    public Location(String address, double latitude, double longitude){
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Getters and setters for id

    public int getId(){
       return id;
    }

    public void setId(int id){
       this.id = id;
    }

    //Getters and setters for address
    public String getAddress(){
       return address;
    }

    public void setAddress(String address){
       this.address = address;
    }

    //Getters and setters for latitude
    public double getLatitude(){
       return latitude;
    }

    public void setLatitude(double latitude){
       this.latitude = latitude;
    }

    //Getters and setters for longitude
    public double getLongitude(){
       return longitude;
    }

    public void setLongitude(double longitude){
       this.longitude = longitude;
    }
}
