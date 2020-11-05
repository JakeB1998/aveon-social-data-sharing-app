package com.example.madcompetition.backend.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.madcompetition.backend.account.Account;
import com.example.madcompetition.backend.utils.SerializationOperations;

import java.util.ArrayList;

public class ServerInformationDatabaseInterface {
    private static final ServerInformationDatabaseInterface ourInstance = new ServerInformationDatabaseInterface();

    public static ServerInformationDatabaseInterface getInstance() {
        return ourInstance;
    }

    private ServerInformationDatabaseInterface() {
    }

    public SQLiteDatabase getReadableDatabase(Context context) {
        FeedReaderDatabase dbHelper = new FeedReaderDatabase(context);
        return dbHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase(Context context) {
        FeedReaderDatabase dbHelper = new FeedReaderDatabase(context);
        return dbHelper.getWritableDatabase();

    }

    public void addAccountToDatabase(Context context, Account account) {
        SQLiteDatabase db = AccountDatabaseInterface.getInstance().getWritableDatabase(context);
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "Account");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "subtitle");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_ID, Integer.toString(account.getAccountID()));
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ, SerializationOperations.serializeObjectToBtyeArray(account));


// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        values.put(FeedReaderContract.FeedEntry._ID, newRowId);
        //int xx = db.update(FeedReaderContract.FeedEntry.TABLE_NAME, values, selection, selectionArgs);
        // Log.i("Databae", "Row was alrtered : " + xx);
    }

    public Account[] readData(Context context) {

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE
        };

// Filter results WHERE "title" = 'My Title'
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {"Account"};

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        SQLiteDatabase db = AccountDatabaseInterface.getInstance().getWritableDatabase(context);
        Cursor cursor = this.queryDatabase(db,null,null);

        Log.i("DataBase", "Number of rows : " + cursor.getCount());


        String[] s1 = cursor.getColumnNames();
        String s;
        s = s1[0];

        ArrayList<Account> accounts = new ArrayList<>(0);
        Account[] returnAccounts;


        if (cursor.moveToFirst() != false) {
            cursor.moveToFirst();


            Log.i("Database", "Row Id : " + cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry._ID)));
            accounts.add((Account) SerializationOperations.deserializeToObject(cursor.getBlob(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ))));

            Log.i("Mine", "From Data base : " + accounts.get(accounts.size() - 1));
            //accounts.add(new Account(Integer.parseInt(accountId), userName, false, AccountType.User, AccountSubType.Personal, phoneNumber, email));
            while (cursor.moveToNext()) {


                accounts.add((Account) SerializationOperations.deserializeToObject(cursor.getBlob(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ))));


                Log.i("Mine", "From Data base : " + accounts.get(accounts.size() - 1));

            }
        }
        int size = 0;
        for (Account c : accounts)
        {
            if (c != null)
            {
                size++;
            }
        }
        returnAccounts = new Account[size];
        for (int i = 0; i < size; i++) {
            if (accounts.get(i)!= null) {
                returnAccounts[i] = accounts.get(i);
            }
        }


        return returnAccounts;
    }


    public boolean updateAccountToDatabase(Context context, Account account)
    {
        String rowNum;
        // Filter results WHERE "title" = 'My Title'
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_ID + " = ?";
        String[] selectionArgs = {Integer.toString(account.getAccountID())};


        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        SQLiteDatabase db = AccountDatabaseInterface.getInstance().getWritableDatabase(context);
        Cursor cursor = this.queryDatabase(db,null,null);


        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ, SerializationOperations.serializeObjectToBtyeArray(account));

        if (cursor.moveToFirst()) {
            if (cursor.getCount() == 1) {

                rowNum = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                String[] x1 = {rowNum};
                return this.updateRow(db, cv, FeedReaderContract.FeedEntry._ID + "= ?", x1);

            } else if (cursor.getCount() == 0) {
                // account not saved

            } else if (cursor.getCount() > 1) {
                // account recorded twice
            } else {
                // cursor is null
            }
        }
        return false;
    }

    public boolean deleteTable()
    {

        return true;

    }

    public boolean deleteRow(SQLiteDatabase db,  String selection, String[] selectionArgs)
    {
        int x =  db.delete(FeedReaderContract.FeedEntry.TABLE_NAME,  selection, selectionArgs);

        return x > 0;
    }

    public boolean updateRow(SQLiteDatabase db, ContentValues cv,  String selection, String[] selectionArgs)
    {


        int xx = db.update(FeedReaderContract.FeedEntry.TABLE_NAME, cv, selection, selectionArgs);
        if (xx > 0)
        {
            return true;
        }

        Log.i("Databae", "Row was alrtered : " + xx);
        return false;


    }

    public Cursor queryDatabase(SQLiteDatabase db, String selection, String[] selectionArgs)
    {
        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                null,           // The array of columns to return (pass null to get all)
                selection,             // The columns for the WHERE clause
                selectionArgs,         // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null             // The sort order
        );

        return cursor;
    }

}
