package com.example.househelper;


public class Supply {
    private String name;
    private String urgency;

    public Supply(String name, String urgency) {
        this.name = name;
        this.urgency = urgency;
    }

    public String getName() {
        return this.name;
    }

    public String getUrgency() {
        return this.urgency;
    }
}
