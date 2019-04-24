package com.example.househelper;

import java.util.ArrayList;

public class User {
    private String name;
    private ArrayList<String> tasks;

    public User(String name, ArrayList<String> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getTasks() {
        return this.tasks;
    }
}
