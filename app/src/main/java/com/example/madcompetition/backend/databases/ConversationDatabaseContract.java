package com.example.madcompetition.backend.databases;

import android.provider.BaseColumns;

public class ConversationDatabaseContract
{

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "Title";
        public static final String COLUMN_NAME_SUBTITLE = "Subtitle";
        public static final String COLUMN_NAME_ACCOUNT_ID = "AccountID";
        public static final String COLUMN_NAME_CONVERSATION_ID = "ConversationID";
        public static final String COLUMN_NAME_CONVERSATION_OBJECT= "ConversationObject";

    }
}
