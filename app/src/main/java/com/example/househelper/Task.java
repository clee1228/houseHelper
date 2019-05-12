package com.example.househelper;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    public String name;
    public String difficulty; //1 = easy, 2 = medium, 3 = hard
    public String frequency;
    public boolean completed;
    public String userEmail = ""; //user's email

    public Task() {

    }

    public Task(String name, String difficulty, String frequency) {
        this.name = name;
        this.difficulty = difficulty;
        this.frequency = frequency;
        this.completed = false;
    }

    public Task(String name, String difficulty, String frequency, boolean completed, String userEmail) {
        this.name = name;
        this.difficulty = difficulty;
        this.frequency = frequency;
        this.completed = completed;
        this.userEmail = userEmail;
    }

    public void markAsComplete() {
        this.completed = true;
    }

    public String getName() {
        return this.name;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public String getFrequency() {
        return this.frequency;
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void markAsIncomplete() {
        this.completed = false;
    }

}
