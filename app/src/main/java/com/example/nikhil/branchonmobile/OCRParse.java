package com.example.nikhil.branchonmobile;

import android.util.Log;

/**
 * Created by Nikhil on 09-03-2017.
 */

public class OCRParse {
    String input;
    public OCRParse(String input){
        this.input = input;
    }
    String[] getDetails(){
        String[] res = input.split("\\s{2,}");
        for(int i=0;i<res.length;i++){
            Log.e("Cheque", res[i]);
        }
        String[] result = {res[1].replaceAll("[^A-Za-z0-9]", ""),
                res[3].replaceAll("[^A-Za-z0-9]", ""),
                res[8].replaceAll("[^A-Za-z0-9]", ""),
                res[11].replaceAll("[^A-Za-z0-9]", "")};
        return result;
    }
}
