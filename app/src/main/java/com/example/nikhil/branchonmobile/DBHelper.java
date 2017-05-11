package com.example.nikhil.branchonmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil on 09-05-2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String name = "fixedaccounts.db";
    public static final String tableName = "fixedaccounts";
    public static final String col_1 = "accNo";
    public static final String col_2 = "amount";
    public DBHelper(Context context) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+tableName+" ("+col_1+" TEXT,"+col_2+" INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+tableName);
        onCreate(db);
    }
    public boolean insertData(String accNo, String amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_1, accNo);
        cv.put(col_2, amount);
        long result = db.insert(tableName, null, cv);
        if(result==-1)
            return false;
        else
            return true;
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+tableName, null);
        return res;
    }
    public boolean dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(tableName, "1", null);
        if(i==0)
            return false;
        else
            return true;
    }
}
