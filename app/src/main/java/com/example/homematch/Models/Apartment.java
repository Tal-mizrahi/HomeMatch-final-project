package com.example.homematch.Models;

import java.util.ArrayList;

public class Apartment extends House{



    private int floorNumber;
    private int apartmentNumber;

    public Apartment() {
        super();
    }

    public Apartment(String uuid, String houseType) {
        super(uuid, houseType);
    }

    public Apartment(String city, String street, String purchaseType
            , String description, String houseType, int streetNumber, int postalCode
            , int numberOfRooms, int areaSize, int price
            , Integer balconyOrGardenSize, boolean hasElevator, boolean hasProtectedRoom
            , boolean hasGarage, boolean hasBalcony, boolean canSmoke
            , boolean petsAllowed, boolean hasParking, boolean isBillsIncluded
            , ArrayList<String> imagesUri, int floorNumber, int apartmentNumber, String brokerId, String uid) {

        super(city, street, purchaseType, description, houseType, streetNumber, postalCode
                , numberOfRooms, areaSize, price, balconyOrGardenSize, hasElevator, hasProtectedRoom
                , hasGarage, hasBalcony, canSmoke, petsAllowed, hasParking, isBillsIncluded
                , imagesUri, brokerId, uid);
        this.floorNumber = floorNumber;
        this.apartmentNumber = apartmentNumber;

    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public Apartment setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
        return this;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public Apartment setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
        return this;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                super.toString() +
                ", floorNumber=" + floorNumber +
                ", apartmentNumber=" + apartmentNumber +
                '}';
    }
}
