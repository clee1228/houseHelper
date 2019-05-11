package com.example.househelper;


public class Supply {
    private String name;
    private String urgency;
    private String price;

    public Supply(String name, String urgency, String price) {
        this.name = name;
        this.urgency = urgency;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public String getPrice() {
        return this.price;
    }

    public String getUrgency() {
        return this.urgency;
    }
}
