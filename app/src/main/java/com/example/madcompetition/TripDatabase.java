package com.example.madcompetition;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TripDatabase extends SQLiteOpenHelper { private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + TripDatabaseContract.FeedEntry.TABLE_NAME + " (" +
                TripDatabaseContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                TripDatabaseContract.FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                TripDatabaseContract.FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TripDatabse.db";

    public TripDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
