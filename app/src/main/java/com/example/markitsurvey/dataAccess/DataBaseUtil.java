package com.example.markitsurvey.dataAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.markitsurvey.models.Survey;

public class DataBaseUtil extends SQLiteOpenHelper {

    private static String DB_NAME = "Markit_V2_db";
    private static int DB_VERSION = 26;

    public DataBaseUtil(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // execute the query string to the database.

        db.execSQL(Survey.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion)
            return;

        // drop tables.
        db.execSQL(Survey.DROP_TABLE);

        onCreate(db);
    }

    public SQLiteDatabase openConnection() {
        SQLiteDatabase dbConnection = this.getWritableDatabase();
        return dbConnection;
    }

    public void closeConnection() {
        this.close();
    }

}
