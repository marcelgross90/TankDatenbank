package org.marcelgross.tankdatenbank.entity;

public class Entry {

    private int id;
    private String gasstation;
    private int day;
    private int month;
    private int year;
    private double liter;
    private double price_liter;
    private int milage;
    private int vehicleID;

    public Entry() {
    }

    public Entry(String gasstation, int day, int month, int year, double liter, double price_liter, int milage, int vehicleID) {
        this.gasstation = gasstation;
        this.day = day;
        this.month = month;
        this.year = year;
        this.liter = liter;
        this.price_liter = price_liter;
        this.milage = milage;
        this.vehicleID = vehicleID;
    }

    public Entry(int id, String gasstation, int day, int month, int year, double liter, double price_liter, int milage, int vehicleID) {
        this.id = id;
        this.gasstation = gasstation;
        this.day = day;
        this.month = month;
        this.year = year;
        this.liter = liter;
        this.price_liter = price_liter;
        this.milage = milage;
        this.vehicleID = vehicleID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGasstation() {
        return gasstation;
    }

    public void setGasstation(String gasstation) {
        this.gasstation = gasstation;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getLiter() {
        return liter;
    }

    public void setLiter(double liter) {
        this.liter = liter;
    }

    public double getPrice_liter() {
        return price_liter;
    }

    public void setPrice_liter(double price_liter) {
        this.price_liter = price_liter;
    }

    public int getMilage() {
        return milage;
    }

    public void setMilage(int milage) {
        this.milage = milage;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }
}
