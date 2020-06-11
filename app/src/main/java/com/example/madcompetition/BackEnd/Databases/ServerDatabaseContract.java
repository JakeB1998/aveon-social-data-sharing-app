package com.example.madcompetition.BackEnd.Databases;

import android.provider.BaseColumns;

public class ServerDatabaseContract {

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "ServerDatabaseEntry";
        public static final String COLUMN_NAME_TITLE = "Title";
        public static final String COLUMN_NAME_SUBTITLE = "Subtitle";
        public static final String COLUMN_NAME_SERVER_OBJ = "Server Object State";


    }
}
