package com.city.guide.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.city.guide.api.types.ApiResults;
import com.city.guide.api.types.DestinationResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toufikzitouni on 14-10-24.
 */
public class CityGuideDBOps {

    public static void insertResults (Context context, ApiResults apiResults) {
        CityGuideSQLiteHelper dbHelper = new CityGuideSQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CityGuideSQLiteHelper.TABLE_NAME, null, null);
        for (ApiResults result : apiResults.getResults()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CityGuideSQLiteHelper.NAME_KEY, result.getName());
            contentValues.put(CityGuideSQLiteHelper.TYPES_KEY,
                    TextUtils.join(",", result.getTypes()));
            contentValues.put(CityGuideSQLiteHelper.RATING_KEY, result.getRating());
            contentValues.put(CityGuideSQLiteHelper.LAT_KEY,
                    result.getGeometry().getLocation().getLat());
            contentValues.put(CityGuideSQLiteHelper.LNG_KEY,
                    result.getGeometry().getLocation().getLng());
            // Insert into DB
            db.insert(CityGuideSQLiteHelper.TABLE_NAME, null, contentValues);
        }
        db.close();
    }

    public static void updateDistances(Context context, DestinationResults destinationResults) {
        CityGuideSQLiteHelper dbHelper = new CityGuideSQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        DestinationResults[] elements = destinationResults.getRows()[0].getElements();

        String[] tableColumns = new String[] {CityGuideSQLiteHelper.ID};

        // Query the database
        Cursor cursor = db.query(CityGuideSQLiteHelper.TABLE_NAME, tableColumns, null, null, null,
                null, null);
        cursor.moveToFirst();
        int i = 0;
        // Iterate the results
        while (!cursor.isAfterLast()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CityGuideSQLiteHelper.DIST_KEY, elements[i].getDistance().getText());
            db.update(CityGuideSQLiteHelper.TABLE_NAME, contentValues,
                    CityGuideSQLiteHelper.ID + "=" +
                            cursor.getInt(cursor.getColumnIndex(CityGuideSQLiteHelper.ID)), null);

            // Move to the next result
            cursor.moveToNext();
            i++;
        }

        cursor.close();
        db.close();
    }

    public static List<ApiResults> getData(Context context) {
        CityGuideSQLiteHelper dbHelper = new CityGuideSQLiteHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<ApiResults> resultList = new ArrayList<ApiResults>();

        // Name of the columns we want to select
        String[] tableColumns = new String[] {CityGuideSQLiteHelper.ID,
                CityGuideSQLiteHelper.NAME_KEY,
                CityGuideSQLiteHelper.TYPES_KEY,
                CityGuideSQLiteHelper.RATING_KEY,
                CityGuideSQLiteHelper.LAT_KEY,
                CityGuideSQLiteHelper.LNG_KEY,
                CityGuideSQLiteHelper.DIST_KEY};

        // Query the database
        Cursor cursor = db.query(CityGuideSQLiteHelper.TABLE_NAME, tableColumns, null, null, null,
                null, CityGuideSQLiteHelper.DIST_KEY);
        cursor.moveToFirst();

        // Iterate the results
        while (!cursor.isAfterLast()) {
            ApiResults apiResults = new ApiResults();
            // Take values from the DB
            apiResults.setName(cursor.getString(cursor.getColumnIndex(
                    CityGuideSQLiteHelper.NAME_KEY)));
            apiResults.setTypes(cursor.getString(cursor.getColumnIndex(
                    CityGuideSQLiteHelper.TYPES_KEY)).split(","));
            apiResults.setRating(cursor.getDouble(cursor.getColumnIndex(
                    CityGuideSQLiteHelper.RATING_KEY)));
            apiResults.setLat(cursor.getDouble(cursor.getColumnIndex(
                    CityGuideSQLiteHelper.LAT_KEY)));
            apiResults.setLng(cursor.getDouble(cursor.getColumnIndex(
                    CityGuideSQLiteHelper.LNG_KEY)));
            apiResults.setDist(cursor.getString(cursor.getColumnIndex(
                    CityGuideSQLiteHelper.DIST_KEY)));

            // Add to the DB
            resultList.add(apiResults);

            // Move to the next result
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return resultList;
    }
}
