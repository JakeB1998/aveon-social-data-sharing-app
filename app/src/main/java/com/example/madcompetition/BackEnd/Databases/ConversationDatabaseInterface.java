package com.example.madcompetition.BackEnd.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.madcompetition.BackEnd.account.Account;
import com.example.madcompetition.BackEnd.messaging.system.Conversation;
import com.example.madcompetition.BackEnd.utils.SerializationOperations;

import java.util.ArrayList;

class ConversationDatabaseInterface {
    private static final ConversationDatabaseInterface ourInstance = new ConversationDatabaseInterface();

    static ConversationDatabaseInterface getInstance() {
        return ourInstance;
    }

    private ConversationDatabaseInterface() {
    }

    public SQLiteDatabase getReadableDatabase(Context context) {
        ConversationDatabaseOpenerHelper dbHelper = new  ConversationDatabaseOpenerHelper(context);
        return dbHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase(Context context) {
        ConversationDatabaseOpenerHelper dbHelper = new  ConversationDatabaseOpenerHelper(context);
        return dbHelper.getWritableDatabase();

    }

    public void addConversationToDatabase(Context context, Conversation convo)
    {
        SQLiteDatabase db = ConversationDatabaseInterface.getInstance().getWritableDatabase(context);
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "Conversation");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "subtitle");
        values.put(ConversationDatabaseContract.FeedEntry.COLUMN_NAME_CONVERSATION_OBJECT, SerializationOperations.serializeObjectToBtyeArray(convo));

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ConversationDatabaseContract.FeedEntry.TABLE_NAME, null, values);
    }

    public Conversation[] readData(Context context)
    {

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE
        };

// Filter results WHERE "title" = 'My Title'
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { "Account" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        SQLiteDatabase db = AccountDatabaseInterface.getInstance().getWritableDatabase(context);
        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                null ,           // The array of columns to return (pass null to get all)
                null ,             // The columns for the WHERE clause
                null,         // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null             // The sort order
        );

        ArrayList<Account> accounts = new ArrayList<>(0);
        Conversation[] returnConversations= null;
        int index = 0;


        if (cursor.moveToFirst() != false) {
            cursor.moveToFirst();
            returnConversations[index] = (Conversation)SerializationOperations.deserializeToObject(cursor.getBlob(cursor.getColumnIndexOrThrow(ConversationDatabaseContract.FeedEntry.COLUMN_NAME_CONVERSATION_OBJECT)));
            index++;

            while (cursor.moveToNext()) {
                returnConversations[index] = (Conversation)SerializationOperations.deserializeToObject(cursor.getBlob(cursor.getColumnIndexOrThrow(ConversationDatabaseContract.FeedEntry.COLUMN_NAME_CONVERSATION_OBJECT)));
                index++;




            }
        }



        return returnConversations;
    }
}
