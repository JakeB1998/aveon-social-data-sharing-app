package com.example.madcompetition.BackEnd.Databases;

import android.provider.BaseColumns;

public class TripDatabaseContract {

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "TripDatabse";
        public static final String COLUMN_NAME_TITLE = "Title";
        public static final String COLUMN_NAME_SUBTITLE = "Subtitle";

    }
}
