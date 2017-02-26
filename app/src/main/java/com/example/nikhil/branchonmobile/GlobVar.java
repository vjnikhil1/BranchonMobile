package com.example.nikhil.branchonmobile;

import android.app.Application;

/**
 * Created by Nikhil on 06-02-2017.
 */

public class GlobVar extends Application {
    protected static String acc_no;

    public String getAcc_no() {
        return acc_no;
    }

    public void setAcc_no(String accNo) {
        this.acc_no = accNo;
    }
}
