package org.marcelgross.tankdatenbank.entity;

public class Vehicle {

    private int id;
    private String name;
    private long millage;

    public Vehicle() {
    }

    public Vehicle( int id, String name, long millage ) {
        this.id = id;
        this.name = name;
        this.millage = millage;
    }

    public Vehicle( String name, long millage ) {
        this.name = name;
        this.millage = millage;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public long getMillage() {
        return millage;
    }

    public void setMillage( long millage ) {
        this.millage = millage;
    }
}
