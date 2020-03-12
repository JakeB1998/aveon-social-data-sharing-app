package com.example.madcompetition.BackEnd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.madcompetition.AccountSubType;
import com.example.madcompetition.AccountType;
import com.example.madcompetition.FeedReaderContract;
import com.example.madcompetition.FeedReaderDatabase;
import com.example.madcompetition.SerializationOperations;

import java.util.ArrayList;

public class AccountDatabaseInterface {
    private static final AccountDatabaseInterface ourInstance = new AccountDatabaseInterface();



    public static AccountDatabaseInterface getInstance() {
        return ourInstance;
    }

    private AccountDatabaseInterface() {
    }

    public SQLiteDatabase getReadableDatabase(Context context) {
        FeedReaderDatabase dbHelper = new FeedReaderDatabase(context);
        return dbHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase(Context context) {
        FeedReaderDatabase dbHelper = new FeedReaderDatabase(context);
        return dbHelper.getWritableDatabase();

    }

    public void addAccountToDatabase(Context context, Account account)
    {
        SQLiteDatabase db = AccountDatabaseInterface.getInstance().getWritableDatabase(context);
        ContentValues values = new ContentValues();
        /*
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "Account");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "subtitle");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_TYPE, "This Account Type");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_USER_FULL_NAME, "This users full name");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_ID, Integer.toString(account.getAccountID()));


         */
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ, SerializationOperations.serializeObjectToBtyeArray(account));




// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
    }

    public Account[] readData(Context context)
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

        Log.i("Mine", "Number of rows : " +  (cursor.getColumnName(4)));


        String[] s1 = cursor.getColumnNames();
        String s;
        s = s1[0];

        ArrayList<Account> accounts = new ArrayList<>(0);
        Account[] returnAccounts;

        String accountType;
        String accountId;
        String userName;
        String fullName;
        String phoneNumber;
        String email;

        if (cursor.moveToFirst() != false) {
            cursor.moveToFirst();
            /*
            accountType = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_TYPE));
            accountId = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_ID));
            userName = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_USER_FULL_NAME));
            phoneNumber = cursor.getString((cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE_NUMBER)));
            email = cursor.getString((cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL)));

             */

            accounts.add((Account)SerializationOperations.deserializeToObject(cursor.getBlob(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ))));

            Log.i("Mine", "From Data base : " + accounts.get(accounts.size() - 1));
            //accounts.add(new Account(Integer.parseInt(accountId), userName, false, AccountType.User, AccountSubType.Personal, phoneNumber, email));
            while (cursor.moveToNext()) {
                /*
                accountType = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_TYPE));
                accountId = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_ID));
                fullName = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_USER_FULL_NAME));
                phoneNumber = cursor.getString((cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE_NUMBER)));
                email = cursor.getString((cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL)));

                accounts.add(new Account(Integer.parseInt(accountId), fullName, false, AccountType.User, AccountSubType.Personal, phoneNumber, email));

                 */
                accounts.add((Account)SerializationOperations.deserializeToObject(cursor.getBlob(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ))));


                Log.i("Mine", "From Data base : " + accounts.get(accounts.size() - 1));

            }
        }

            returnAccounts = new Account[accounts.size()];
            for (int i = 0; i < accounts.size(); i++) {
                returnAccounts[i] = accounts.get(i);
            }


        return returnAccounts;
    }


}
