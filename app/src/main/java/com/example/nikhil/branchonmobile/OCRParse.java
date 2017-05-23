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
        String[] result = new String[10];
        String[] res = input.split("\\s{2,}");
        String[] test = new String[10];
        for(int i=0,j=0;i<res.length;i++) {
            String s = res[i].replaceAll("[^0-9]","");
            if(s.length()>0&&!s.contains("1000000")) {
                test[j] = s;
                j++;
            }
        }
        /*for(int i=0;i<test.length;i++)
        Log.e("newlogic",test[i]);
        for(int i=0,j=0;i<res.length;i++){
            res[i] = res[i].replaceAll("[^A-Za-z0-9\\s]","");
            Log.e("Transaction", res[i]);
            if(res[i].trim().toLowerCase().contains("pay")){
                if(res[i].length()>3){
                    result[j] = res[i].toLowerCase().replace("pay","").trim();
                    Log.e("Dunkir", result[j]);
                    j++;
                }
                else
                {
                    result[j] = res[i+1];
                    Log.e("Dunkir", result[j]);
                    j++;
                }
            }
            else if(res[i].trim().toLowerCase().contains("first bank")){
                if(res[i+2].trim().toLowerCase().contains("ddmmyyyy")||
                    res[i+2].trim().toLowerCase().contains("odmmyyyy")||
                        res[i+2].trim().toLowerCase().contains("dommyyyy")) {
                    result[j] = res[i + 1];
                    Log.e("Dunkir", result[j]);
                    j++;
                }
            }
            else if(res[i].trim().toLowerCase().contains("rupees")){
                result[j] = res[i+1].replace("*","").replace(" ","").replace(",","").trim();
                Log.e("Dunkir amount", result[j]+res[i+1]);
                j++;
            }
            else if(res[i].trim().toLowerCase().contains("ac no")
                    ||res[i].trim().toLowerCase().contains("acno")){
                if(res[i].length()>7){
                    result[j] = res[i].toLowerCase().replace("ac no","").trim();
                    Log.e("Dunkir", result[j]);
                    j++;
                }
                else{
                    result[j] = res[i+1];
                }
            }
        }*/

        /*result = {res[1].replaceAll("[^A-Za-z0-9]", ""),
                res[3].replaceAll("[^A-Za-z0-9]", ""),
                res[7].replaceAll("[^A-Za-z0-9]", ""),
                res[8].replaceAll("[^A-Za-z0-9]", ""),
                res[11].replaceAll("[^A-Za-z0-9]", "")};*/
        return test;
    }
}
