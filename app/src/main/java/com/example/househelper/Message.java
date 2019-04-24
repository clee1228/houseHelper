package com.example.househelper;

import java.util.Date;

public class Message {

    public String text;
    public String user;
    public Date date;

    Message(String text, String user, Date date) {
        this.text = text;
        this.user = user;
        this.date = date;
    }

    protected String elapsedTimeString() {
        long diff = new Date().getTime() - date.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        int daysInt = Math.round(days);
        int hoursInt = Math.round(hours);
        int minutesInt = Math.round(minutes);
        if (daysInt == 1) {
            return "1 day";
        } else if (daysInt > 1) {
            return Integer.toString(daysInt) + " days";
        } else if (hoursInt == 1) {
            return "1 hour";
        } else if (hoursInt > 1) {
            return Integer.toString(hoursInt) + " hours";
        } else {
            return "less than an hour";
        }
    }


}
