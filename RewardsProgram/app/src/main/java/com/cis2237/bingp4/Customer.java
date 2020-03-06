package com.cis2237.bingp4;

public class Customer {

    private int id;
    private String name;
    private String airline;
    private String status;
    private String miles;

    public Customer() {}

    public Customer(int id, String name, String airline, String status, String miles) {
        this.id = id;
        this.name = name;
        this.airline = airline;
        this.status = status;
        this.miles = miles;
    }

    public String getMiles() { return miles; }

    public void setMiles(String miles) { this.miles = miles; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
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

}
