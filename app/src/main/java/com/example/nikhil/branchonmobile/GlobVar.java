package com.example.nikhil.branchonmobile;

import android.app.Application;

/**
 * Created by Nikhil on 06-02-2017.
 */

public class GlobVar extends Application {
    private String acc_no;

    public String getAcc_no() {
        return acc_no;
    }

    public void setAcc_no(String acc_no) {
        this.acc_no = acc_no;
    }
}
