package com.example.nikhil.branchonmobile;

/**
 * Created by Nikhil on 07-03-2017.
 */

public class Cheques {
    String pay, amount, issue, status, progress;

    public Cheques(String pay, String amount, String issue, String status, String progress) {
        this.pay = pay;
        this.amount = amount;
        this.issue = issue;
        this.status = status;
        this.progress = progress;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
