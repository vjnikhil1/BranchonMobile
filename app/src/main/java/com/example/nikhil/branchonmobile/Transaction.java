package com.example.nikhil.branchonmobile;

/**
 * Created by Nikhil on 23-03-2017.
 */

public class Transaction {
    int img;
    String title;
    String accNo;
    String date;

    public Transaction(int img, String title, String accNo, String date, String amount) {
        this.img = img;
        this.title = title;
        this.accNo = accNo;
        this.date = date;
        this.amount = amount;
    }

    String amount;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
