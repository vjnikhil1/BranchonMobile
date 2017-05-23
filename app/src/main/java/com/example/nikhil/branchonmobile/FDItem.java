package com.example.nikhil.branchonmobile;

/**
 * Created by Nikhil on 21-05-2017.
 */

public class FDItem {
    String id;
    String amount;
    String date;
    String time;

    public FDItem(String id, String amount, String date, String time, String period) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.period = period;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    String period;
}
