package com.city.guide.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CityGuideSQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "city_guide_db";
    public static final String TABLE_NAME = "CityGuide";
    public static final String ID = "_id";
    public static final String NAME_KEY = "name";
    public static final String TYPES_KEY = "types";
    public static final String RATING_KEY = "rating";
    public static final String LAT_KEY = "lat";
    public static final String LNG_KEY = "lng";
    public static final String DIST_KEY = "dist";

    public CityGuideSQLiteHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_KEY + " TEXT NOT NULL," +
                TYPES_KEY + " TEXT NOT NULL," +
                RATING_KEY + " REAL," +
                LAT_KEY + " REAL," +
                LNG_KEY + " REAL," +
                DIST_KEY + " TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}