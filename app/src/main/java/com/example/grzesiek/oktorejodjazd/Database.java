package com.example.grzesiek.oktorejodjazd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Grzesiek on 2017-10-24.
 */


// brak zabezpiecznia przed wciskaniem zlych danych i SQLInjection

public class Database extends SQLiteOpenHelper {
    public Database(Context con){
        super(con, "przystanki.db" , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("" +
                "CREATE TABLE przystanki(" +
                "id_przystanku INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nazwa_przystanku TEXT NOT NULL UNIQUE," +
                "url TEXT NOT NULL," +
                "ztm_source_code TEXT NOT NULL);" +
                "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    // DANE MUSZA BYC POPRAWNE
    public void addBusStop(String name, String URL, String source_code){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("nazwa_przystanku" , name);
        row.put("url", URL);
        row.put("ztm_source_code", source_code);
        // db.insertOrThrow("przystanki", null, row);
        // ta metoda, po to, ze jak postarasz sie wrzucic cos z nazwa przystnaku,
        // ktora juz jest w bazie danych, to zrobi update calosci
        db.insertWithOnConflict("przystanki", null, row, SQLiteDatabase.CONFLICT_REPLACE);
    }

    // PRZY ZLYCH DANYCH ZWRACA NULLA
    public String getResource(String name, String col){
        // moze jako col wez enuma, a jak nie to dodaj obsluge bledu jak sie wpisze jakas glupote
        // select rowny null case - ogarnij
        SQLiteDatabase db = getReadableDatabase();
        String[] cols = {col};
        try{
            Cursor cursor = db.query("przystanki",
                    cols,
                    "nazwa_przystanku='"+name+"'",
                    null, null, null, null);
            cursor.moveToNext();// ????
            return cursor.getString(0);
        } catch (SQLiteException ex ){
            return null;
        }

    }
}
