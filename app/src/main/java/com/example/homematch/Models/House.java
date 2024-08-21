package com.example.homematch.Models;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class House {

    private String city;
    private String street;
    private String purchaseType; // Rent or Sale
    private String description;
    private String houseType; // Apartment, Duplex, Penthouse, Garden, Private House

    private String agentId;
    private String uuid;

    private int streetNumber;
    private int numberOfRooms;

    private int areaSize;
    private int price;
    private Integer balconyOrGardenSize;

    private boolean hasElevator;
    private boolean hasProtectedRoom;
    private boolean hasGarage;
    private boolean hasBalcony;
    private boolean canSmoke;
    private boolean petsAllowed;
    private boolean hasParking;
    private boolean billsIncluded;

    private ArrayList<String> imagesUrl;

    private String openHouseDate = null;
    private String openHouseTime = null;
    private HashMap<String, String> openHouseSignUps = new HashMap<>(); // Map of client uid that sign up to open house

    public House() {
    }

    public House(String uuid, String houseType){
        this.uuid = uuid;
        this.houseType = houseType;
    }

    public House(String city, String street, String purchaseType
            , String description, String houseType, int streetNumber
            , int numberOfRooms, int areaSize, int price, Integer balconyOrGardenSize
            , boolean hasElevator, boolean hasProtectedRoom, boolean hasGarage
            , boolean hasBalcony, boolean canSmoke, boolean petsAllowed
            , boolean hasParking, boolean isBillsIncluded, ArrayList<String> imagesUri
            , String agentId, String uuid) {
        this.city = city;
        this.street = street;
        this.purchaseType = purchaseType;
        this.description = description;
        this.streetNumber = streetNumber;
        this.numberOfRooms = numberOfRooms;
        this.areaSize = areaSize;
        this.price = price;
        this.hasElevator = hasElevator;
        this.hasProtectedRoom = hasProtectedRoom;
        this.hasGarage = hasGarage;
        this.hasBalcony = hasBalcony;
        this.canSmoke = canSmoke;
        this.petsAllowed = petsAllowed;
        this.hasParking = hasParking;
        this.billsIncluded = isBillsIncluded;
        this.imagesUrl = imagesUri;
        this.agentId = agentId;
        this.uuid = uuid;
        this.houseType = houseType;
        this.balconyOrGardenSize = balconyOrGardenSize;
    }

    public void addClientToOpenHouse(String clientUid){
        if(openHouseSignUps == null)
            openHouseSignUps = new HashMap<>();
        openHouseSignUps.put(clientUid, clientUid);
        Log.d("open house", "added client " + clientUid);
    }

    public void removeClientFromOpenHouse(String clientUid){
        openHouseSignUps.remove(clientUid);
    }

    public void resetOpenHouseData(){
        this.openHouseDate = null;
        this.openHouseTime = null;
        this.openHouseSignUps = new HashMap<>();
    }

    public String getOpenHouseDate() {
        return openHouseDate;
    }

    public House setOpenHouseDate(String openHouseDate) {
        this.openHouseDate = openHouseDate;
        return this;
    }

    public String getOpenHouseTime() {
        return openHouseTime;
    }

    public House setOpenHouseTime(String openHouseTime) {
        this.openHouseTime = openHouseTime;
        return this;
    }

    public String getCity() {
        return city;
    }

    public House setCity(String city) {
        this.city = city;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public House setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getHouseType() {
        return houseType;
    }

    public House setHouseType(String houseType) {
        this.houseType = houseType;
        return this;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public House setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public House setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public House setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }


    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public House setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
        return this;
    }

    public int getAreaSize() {
        return areaSize;
    }

    public House setAreaSize(int areaSize) {
        this.areaSize = areaSize;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public House setPrice(int price) {
        this.price = price;
        return this;
    }

    public boolean isHasElevator() {
        return hasElevator;
    }

    public House setHasElevator(boolean hasElevator) {
        this.hasElevator = hasElevator;
        return this;
    }

    public boolean isHasProtectedRoom() {
        return hasProtectedRoom;
    }

    public House setHasProtectedRoom(boolean hasProtectedRoom) {
        this.hasProtectedRoom = hasProtectedRoom;
        return this;
    }

    public boolean isHasGarage() {
        return hasGarage;
    }

    public House setHasGarage(boolean hasGarage) {
        this.hasGarage = hasGarage;
        return this;
    }

    public boolean isHasBalcony() {
        return hasBalcony;
    }

    public House setHasBalcony(boolean hasBalcony) {
        this.hasBalcony = hasBalcony;
        return this;
    }

    public boolean isCanSmoke() {
        return canSmoke;
    }

    public House setCanSmoke(boolean canSmoke) {
        this.canSmoke = canSmoke;
        return this;
    }

    public boolean isPetsAllowed() {
        return petsAllowed;
    }

    public House setPetsAllowed(boolean petsAllowed) {
        this.petsAllowed = petsAllowed;
        return this;
    }

    public boolean isHasParking() {
        return hasParking;
    }

    public House setHasParking(boolean hasParking) {
        this.hasParking = hasParking;
        return this;
    }

    public boolean isBillsIncluded() {
        return billsIncluded;
    }

    public House setBillsIncluded(boolean billsIncluded) {
        this.billsIncluded = billsIncluded;
        return this;
    }

    public ArrayList<String> getImagesUrl() {
        return imagesUrl;
    }

    public House setImagesUrl(ArrayList<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
        return this;
    }

    public String getAgentId() {
        return agentId;
    }

    public House setAgentId(String agentId) {
        this.agentId = agentId;
        return this;
    }

    public HashMap<String, String> getOpenHouseSignUps() {
        return openHouseSignUps;
    }

    public House setOpenHouseSignUps(HashMap<String, String> openHouseSignUps) {
        this.openHouseSignUps = openHouseSignUps;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public Integer getBalconyOrGardenSize() {
        return balconyOrGardenSize;
    }

    public House setBalconyOrGardenSize(Integer balconyOrGardenSize) {
        this.balconyOrGardenSize = balconyOrGardenSize;
        return this;
    }

    public House setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }


    @Override
    public String toString() {
        return "House{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", purchaseType='" + purchaseType + '\'' +
                ", description='" + description + '\'' +
                ", houseType='" + houseType + '\'' +
                ", agentId='" + agentId + '\'' +
                ", uuid='" + uuid + '\'' +
                ", streetNumber=" + streetNumber +
                ", numberOfRooms=" + numberOfRooms +
                ", areaSize=" + areaSize +
                ", price=" + price +
                ", balconyOrGardenSize=" + balconyOrGardenSize +
                ", hasElevator=" + hasElevator +
                ", hasProtectedRoom=" + hasProtectedRoom +
                ", hasGarage=" + hasGarage +
                ", hasBalcony=" + hasBalcony +
                ", canSmoke=" + canSmoke +
                ", petsAllowed=" + petsAllowed +
                ", hasParking=" + hasParking +
                ", isBillsIncluded=" + billsIncluded +
                ", imagesUrl=" + imagesUrl +
                ", openHouseDate='" + openHouseDate + '\'' +
                ", openHouseTime='" + openHouseTime + '\'' +
                ", openHouseSignUps=" + openHouseSignUps +
                '}';
    }
}
