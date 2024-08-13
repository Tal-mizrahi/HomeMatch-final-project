package com.example.homematch.Models;

import java.util.ArrayList;

public class PrivateHouse extends House {

    public PrivateHouse() {
        super();
    }

    public PrivateHouse(String uuid, String houseType) {
        super(uuid, houseType);
    }

    public PrivateHouse(String city, String street, String purchaseType
            , String description, String houseType, int streetNumber
            , int postalCode, int numberOfRooms, int areaSize, int price
            , Integer balconyOrGardenSize, boolean hasElevator, boolean hasProtectedRoom
            , boolean hasGarage, boolean hasBalcony, boolean canSmoke
            , boolean petsAllowed, boolean hasParking, boolean isBillsIncluded
            , ArrayList<String> imagesUri, String brokerId, String uid) {
        super(city, street, purchaseType, description, houseType, streetNumber, postalCode
                , numberOfRooms, areaSize, price, balconyOrGardenSize, hasElevator, hasProtectedRoom
                , hasGarage, hasBalcony, canSmoke, petsAllowed, hasParking
                , isBillsIncluded, imagesUri, brokerId, uid);
    }

    @Override
    public String toString() {
        return "PrivateHouse{ "
                +super.toString()
                +" }";
    }
}
