package com.example.househelper;

public class Task {
    public String name;
    public int difficulty; //1 = easy, 2 = medium, 3 = hard
    public String frequency;

    public Task() {

    }

    public Task(String name, int difficulty, String frequency) {
        this.name = name;
        this.difficulty = difficulty;
        this.frequency = frequency;
    }

}

//enum Frequency {
//    DAILY, WEEKLY, BIWEEKLY, MONTHLY;
//}