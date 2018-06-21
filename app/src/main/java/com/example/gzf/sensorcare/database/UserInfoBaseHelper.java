package com.example.gzf.sensorcare.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gzf.sensorcare.database.UserInfoDbSchema.UserInfoTable;

public class UserInfoBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "sensorCareBase.db";

    public UserInfoBaseHelper(Context context){
        super(context, DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + UserInfoTable.NAME + "(" +
                " _id integer primary key autoincrement, " + UserInfoTable.Cols.BLEID + ", " +
                UserInfoTable.Cols.ISWATCHED + ", " +
                UserInfoTable.Cols.EXISTFLAG + ", " +
                UserInfoTable.Cols.HELPTEXT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
