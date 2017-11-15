package com.example.grzesiek.oktorejodjazd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static Database instance;

    static Database getInstance(Context con) {
        if(instance == null && con != null){
            instance = new Database(con);
            return instance;
        } else if(con == null && instance == null) {
            throw new IllegalArgumentException("Context cannot be null if an instance hasn't been initiated ");
        } else {
            return instance;
        }
    }

    private Database(Context con){
        super(con, "przystanki.db" , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("" +
                "CREATE TABLE przystanki(" +
                "id_przystanku INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nazwa_przystanku TEXT NOT NULL UNIQUE," +
                "url TEXT NOT NULL);"+
                "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // ...
    }

    void addBusStop(String name, String URL){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("nazwa_przystanku" , name);
        row.put("url", URL);
        db.insertWithOnConflict("przystanki", null, row, SQLiteDatabase.CONFLICT_REPLACE);
    }

    String getURL(String name){
        SQLiteDatabase db = getReadableDatabase();
        String[] cols = {"url"};
        try {
            Cursor cursor = db.query("przystanki",
                    cols,
                    "nazwa_przystanku='"+name+"'",
                    null, null, null, null);
            cursor.moveToNext();
            String temp = cursor.getString(0);
            cursor.close();
            return temp;
        } catch (SQLiteException ex ){
            ex.printStackTrace();
            return "sqlerr";
        } catch (CursorIndexOutOfBoundsException ex){
            throw new CursorIndexOutOfBoundsException("");
            //return "curerr";
        }
    }

    ArrayList<String> getBusStopsNames(){
        ArrayList<String> names = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] cols = {"nazwa_przystanku"};
        Cursor cursor = db.query("przystanki",
                cols,null, null, null, null,null
                );
        while(cursor.moveToNext()){
            names.add(cursor.getString(0));
        }
        cursor.close();
        return names;
    }


    boolean inDatabase(String site){
        SQLiteDatabase db = getReadableDatabase();
        String[] cols = {"url"};
        Cursor cursor = db.query("przystanki",
                cols,
                "url='"+site+"'",
                null,null,null,null);
        if(cursor.getCount() == 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    boolean delete(String name){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("przystanki","nazwa_przystanku=\""+name+"\"",null) > 0;
    }
}