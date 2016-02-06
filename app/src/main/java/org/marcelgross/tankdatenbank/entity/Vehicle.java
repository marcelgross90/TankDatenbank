package org.marcelgross.tankdatenbank.entity;

public class Vehicle {

    private int id;
    private String name;
    private long milage;

    public Vehicle() {
    }

    public Vehicle(int id, String name, long milage) {
        this.id = id;
        this.name = name;
        this.milage = milage;
    }

    public Vehicle(String name, long milage){
        this.name = name;
        this.milage = milage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMilage() {
        return milage;
    }

    public void setMilage(long milage) {
        this.milage = milage;
    }
}
