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
        for(int i=0,j=0;i<res.length;i++){
            res[i] = res[i].replaceAll("[^A-Za-z0-9\\s]","");
            Log.e("Transaction", res[i]);
            if(res[i].trim().toLowerCase().contains("pay")){
                if(res[i].length()>3){
                    result[j] = res[i].toLowerCase().replace("pay","").trim();
                    j++;
                }
                else
                {
                    result[j] = res[i+1];
                    j++;
                }
            }
            else if(res[i].trim().toLowerCase().contains("jordan bank")){
                if(res[i+2].trim().toLowerCase().contains("ddmmyyyy")) {
                    result[j] = res[i + 1];
                    j++;
                }
            }
            else if(res[i].trim().toLowerCase().contains("rupees")){
                result[j] = res[i+1].replace(",","");
                j++;
            }
            else if(res[i].trim().toLowerCase().contains("ac no")){
                if(res[i].length()>7){
                    result[j] = res[i].toLowerCase().replace("ac no","").trim();
                    j++;
                }
                else{
                    result[j] = res[i+1];
                }
            }
        }

        /*result = {res[1].replaceAll("[^A-Za-z0-9]", ""),
                res[3].replaceAll("[^A-Za-z0-9]", ""),
                res[7].replaceAll("[^A-Za-z0-9]", ""),
                res[8].replaceAll("[^A-Za-z0-9]", ""),
                res[11].replaceAll("[^A-Za-z0-9]", "")};*/
        return result;
    }
}
