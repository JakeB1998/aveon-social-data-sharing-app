package com.example.madcompetition.backend.databases;

import android.provider.BaseColumns;

public class FeedReaderContract
{
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "Title";
        public static final String COLUMN_NAME_SUBTITLE = "Subtitle";
        public static final String COLUMN_NAME_ACCOUNT_ID = "AccountID";
        public static final String COLUMN_NAME_ACCOUNT_TYPE = "AccountType";
        public static final String COLUMN_NAME_ACCOUNT_SUBTYPE = "AccountSubType";
        public static final String COLUMN_NAME_ACCOUNT_USER_FULL_NAME = "Name";
        public static final String COLUMN_NAME_PHONE_NUMBER = "PhoneNumber";
        public static final String COLUMN_NAME_EMAIL = "Email";
        public static final String COLUMN_NAME_OBJ = "Object";

}
}
