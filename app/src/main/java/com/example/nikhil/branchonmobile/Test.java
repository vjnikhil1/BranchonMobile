package com.example.nikhil.branchonmobile;


/**
 * Created by Nikhil on 09-03-2017.
 */

public class Test {
    public static void main(String[] args){
        String s = "\uFEFF JORDAN BANK                                                               11/02/2017            \n" +
                "                                                                                                                                                                     \n" +
                "                                                                                                                                               DDMMYYVY              \n" +
                "                                                                                                                                                                     \n" +
                "                                                                                    RICK GRIMES                                                                      \n" +
                "                                                                     PAY                                                                        OR BEARER            \n" +
                "                                                                                                                                                                     \n" +
                "                                                                                                                                                      ^ 2,30,500    \n" +
                "                                                                    RUPEES                                                                                          \n" +
                "                                                                                                                                                                     \n" +
                "                                                                                                                                                                     \n" +
                "                                                                                                                                                                     \n" +
                "                                                                                                                                                                     \n" +
                "                                                                        A/C No.             10114567821    VALID FOR Rs. 1000000/- & UNDER                           \n" +
                "                                                                                                                            ■Cftris Mftrbbv                          \n" +
                "                                                                                                                                                                     \n" +
                "                                                                                                                                                                     \n" +
                "                                                                                                                                                                     \n" +
                "                                                                                                                           Please sign above                         \n" +
                "                                                                                                                                                                     \n" +
                "                                                                                                                                                                     \n" +
                "                                                                                                                                                                     \n" +
                "                                                                                                                                                                     \n" +
                "                                                                                                          ii- 9500 20 ip HHIHIIHHB IIHHIHI 91                        ";
        String[] res = s.split("\\s+");
        for(int i=0;i<res.length;i++)
        {
            System.out.println(res[i]);
        }
    }
}
