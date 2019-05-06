package com.example.househelper;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public String name;
    public String email;
    public ArrayList<Task> tasks;
    public int score;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.tasks = new ArrayList<>();
        this.score = 0;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    public int getScore() {
        return this.score;
    }
}
