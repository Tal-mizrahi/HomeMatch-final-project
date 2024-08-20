package com.example.homematch.Models;

public class Agent extends User {
    private String agencyName;
    private int numOfRent;
    private int numOfSale;

    public Agent(String firstName, String lastName, String email, String phoneNumber
            , String password, String uid, String imageUrl, String agencyName) {
        super(firstName, lastName, email, phoneNumber, password,uid,imageUrl);
        this.agencyName = agencyName;
        this.numOfRent = 0;
        this.numOfSale = 0;
    }

    public Agent() {
        super();
    }

    // Getter and setter for agencyName
    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public int getNumOfSale() {
        return numOfSale;
    }

    public Agent setNumOfSale(int numOfSale) {
        this.numOfSale = numOfSale;
        return this;
    }

    public int getNumOfRent() {
        return numOfRent;
    }

    public Agent setNumOfRent(int numOfRent) {
        this.numOfRent = numOfRent;
        return this;
    }

    public void addSaleProperty(){
        this.numOfSale++;
    }

    public void addRentProperty(){
        this.numOfRent++;
    }

    public void propertySold(){
        this.numOfSale--;
    }

    public void propertyRented(){
        this.numOfRent--;
    }

    @Override
    public String toString() {
        return "Agent{" +
                super.toString() +
                "agencyName='" + agencyName + '\'' +
                '}';
    }
}