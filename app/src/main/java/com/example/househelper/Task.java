package com.example.househelper;

public class Task {
    public String name;
    public String difficulty; //1 = easy, 2 = medium, 3 = hard
    public String frequency;
    public boolean completed;

    public Task() {

    }

    public Task(String name, String difficulty, String frequency) {
        this.name = name;
        this.difficulty = difficulty;
        this.frequency = frequency;
        this.completed = false;
    }

    public void markAsComplete() {
        this.completed = true;
    }

    public String getName() {
        return this.name;
    }

}

//enum Frequency {
//    DAILY, WEEKLY, BIWEEKLY, MONTHLY;
//}