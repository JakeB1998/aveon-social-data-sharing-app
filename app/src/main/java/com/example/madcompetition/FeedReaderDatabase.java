package com.example.madcompetition;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedReaderDatabase extends SQLiteOpenHelper

{

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_ID + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_TYPE + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE_NUMBER + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ + " NONE," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_USER_FULL_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public FeedReaderDatabase(Context context)
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
