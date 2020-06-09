package com.androidbroadcast.reciever;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataController {
    public static final String CONTACTS_COLUMN_NAME = "Name";
    public static final String CONTACTS_COLUMN_EMAIL = "Email";
    public static final String CONTACTS_COLUMN_PASSWORD = "Password";
    public static final String TABLE_NAME = "Login_Table";
    public static final String DATABASE_NAME = "Login.db";
    public static final int DATABASE_VERSION = 4;
    public static final String TABLE_CREATE = "create table Login_Table (Name text not null, Email text not null, Password not null);";

    public static final String NOTES_NAME = "Place_Name";
    public static final String NOTES_NOTE = "Place_Note";
    public static final String NOTES_EMAIL = "Place_Email";
    public static final String NOTES_TABLE_NAME = "Notes_Table";
    public static final String NOTES_TABLE_CREATE = "create table Notes_Table (Place_Name text not null, Place_Note text not null, Place_Email text not null);";


    public static final String FAV_EMAIL = "Fav_Email";
    public static final String FAV_PLACE_ID = "placeId";
    public static final String FAV_PHOTO_REFERENCE_ID = "photoReferenceId";
    public static final String FAV_NAME = "name";
    public static final String FAV_LOCATION = "location";
    public static final String FAV_RATING = "rating";
    public static final String FAV_CATEGORY = "category";
    public static final String FAV_OPEN_NOW = "openNow";
    public static final String FAV_LAT = "lat";
    public static final String FAV_LON = "lon";
    public static final String FAV_TABLE_NAME = "Fav_Table";
    public static final String FAV_TABLE_CREATE = "create table Fav_Table (Fav_Email text not null," +
            "placeId text not null, " +
            "photoReferenceId text not null, " +
            "name text not null, " +
            "location text not null, " +
            "rating text not null, " +
            "category text not null, " +
            "openNow text not null, " +
            "lat text not null, " +
            "lon text not null);" +
            "";

    DataBaseHelper dbHelper;
    Context context;
    SQLiteDatabase db;

    public DataController(Context context) {
        this.context = context;
        dbHelper = new DataBaseHelper(context);
    }

    public DataController open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(String name, String email, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("password", password);
        contentValues.put("email", email);
        return db.insertOrThrow(TABLE_NAME, null, contentValues);
    }

    public long addNotes(String place_name, String place_note) {
        String email_LoggedIn = getEmail();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTES_NAME, place_name);
        contentValues.put(NOTES_NOTE, place_note);
        contentValues.put(NOTES_EMAIL, email_LoggedIn);
        return db.insertOrThrow(NOTES_TABLE_NAME, null, contentValues);
    }

    public long addFav(String placeId, String photoReferenceId, String name, String location, String rating, String category, String openNow, String lat, String lon) {
        String email_LoggedIn = getEmail();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FAV_EMAIL, email_LoggedIn);
        contentValues.put(FAV_PLACE_ID, placeId);
        contentValues.put(FAV_PHOTO_REFERENCE_ID, photoReferenceId);
        contentValues.put(FAV_NAME, name);
        contentValues.put(FAV_LOCATION, location);
        contentValues.put(FAV_RATING, rating);
        contentValues.put(FAV_CATEGORY, category);
        contentValues.put(FAV_OPEN_NOW, openNow);
        contentValues.put(FAV_LAT, lat);
        contentValues.put(FAV_LON, lon);
        return db.insertOrThrow(FAV_TABLE_NAME, null, contentValues);
    }

    public Cursor retrieve(String email) {
        Log.d("retr", "retrieve: " + email);
        Cursor res = db.rawQuery("select * from Login_Table where Email = ?", new String[]{email});
        return res;
    }

    public String getEmail() {
        String rv = "test@gmail.com";
        Cursor res = db.rawQuery("select Email from Login_Table", new String[]{});
        if (res.moveToFirst()) {
            rv = res.getString(res.getColumnIndex(CONTACTS_COLUMN_EMAIL));
        }
        return rv;
    }

    public String getNotes(String place_name) {
        String email_LoggedIn = getEmail();
        String rv = "Add Notes Here";
        Log.d("retr", "retrieve: " + place_name);
        Cursor res = db.rawQuery("select * from Notes_Table where Place_Name = ? and Place_Email = ?", new String[]{place_name, email_LoggedIn});
        if (res.moveToFirst()) {
            rv = res.getString(res.getColumnIndex(NOTES_NOTE));
        }
        Log.d("retr", "retrieved Notes: " + rv);
        return rv;
    }

    public boolean checkFav(String fav_name) {
        String email_LoggedIn = getEmail();
        Cursor res = db.rawQuery("select * from Fav_Table where name = ? and Fav_Email = ?", new String[]{fav_name, email_LoggedIn});
        if (res.getCount() <= 0) {
            return false;
        } else
            return true;
    }

    public boolean removeFav(String fav_name) {
        return db.delete(FAV_TABLE_NAME, FAV_NAME + " LIKE '%" + fav_name + "%'", null) > 0;
    }

    public class FavsData {
        String placeId;
        String photoReferenceId;
        String name;
        String location;
        String rating;
        String category;
        String openNow;
        String lat;
        String lon;


        public FavsData(String placeId, String photoReferenceId, String name, String location, String rating, String category, String openNow, String lat, String lon) {
            this.placeId = placeId;
            this.photoReferenceId = photoReferenceId;
            this.name = name;
            this.location = location;
            this.rating = rating;
            this.category = category;
            this.openNow = openNow;
            this.lat = lat;
            this.lon = lon;

        }
    }

    public List<FavsData> getFavList() {
        String email_LoggedIn = getEmail();
        List<FavsData> fav_list = new ArrayList();

        Cursor res = db.rawQuery("select * from Fav_Table where Fav_Email = ?", new String[]{email_LoggedIn});

        while (res.moveToNext()) {
            String placeId = res.getString(res.getColumnIndex(FAV_PLACE_ID));
            String photoReferenceId = res.getString(res.getColumnIndex(FAV_PHOTO_REFERENCE_ID));
            String name = res.getString(res.getColumnIndex(FAV_NAME));
            String location = res.getString(res.getColumnIndex(FAV_LOCATION));
            String rating = res.getString(res.getColumnIndex(FAV_RATING));
            String category = res.getString(res.getColumnIndex(FAV_CATEGORY));
            String openNow = res.getString(res.getColumnIndex(FAV_OPEN_NOW));
            String lat = res.getString(res.getColumnIndex(FAV_LAT));
            String lon = res.getString(res.getColumnIndex(FAV_LON));

            FavsData data = new FavsData(placeId, photoReferenceId, name, location, rating, category, openNow, lat, lon);
            fav_list.add(data);
        }
        return fav_list;
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            try {
                db.execSQL(TABLE_CREATE);
                db.execSQL(NOTES_TABLE_CREATE);
                db.execSQL(FAV_TABLE_CREATE);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS Login_Table");
            db.execSQL("DROP TABLE IF EXISTS Notes_Table");
            db.execSQL("DROP TABLE IF EXISTS Fav_Table");
            onCreate(db);
        }

    }

}