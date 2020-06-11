package com.example.madcompetition.BackEnd.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.madcompetition.BackEnd.account.Account;
import com.example.madcompetition.BackEnd.AppManager;
import com.example.madcompetition.BackEnd.utils.SerializationOperations;

import java.util.ArrayList;

public class AccountDatabaseInterface {
    private static final AccountDatabaseInterface ourInstance = new AccountDatabaseInterface();


    private final String SUBTITLE_FOR_ACCOUNT = "Logged In Account";

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
        db.close();
    }


    public void commitLoggedInAccount(Account account, Context context) {
        // check if one already exists

        Log.i("Database", Boolean.toString(account.equals(AppManager.getInstance().getCurrentAccountLoggedIn())));

        SQLiteDatabase db = AccountDatabaseInterface.getInstance().getWritableDatabase(context);
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " = ?";
        String[] selectionArgs = {SUBTITLE_FOR_ACCOUNT};

        Cursor cursor = this.queryDatabase(db,selection,selectionArgs);

        ContentValues values = new ContentValues();


        if (cursor.moveToFirst()) {

            if (cursor.getCount() < 2) {

                String x = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_ID));
                int z = Integer.parseInt(x);
                if (account.getAccountID() == z) {
                    Log.i("Database", "Account already marked as logged in in database");
                } else {


                    //cursor.moveToFirst();
                    String selectionNew = FeedReaderContract.FeedEntry._ID + " = ?";
                    String[] selectionArgsNew = {cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry._ID))};
                    this.updateRow(db,values,selectionNew,selectionArgs);

                    Log.i("Database", "Row Id : " + cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE)));
                }

            } else {


                Log.i("Database", "row counts is more than one");

            }
        } else {

            Cursor cursorNew = this.queryDatabase(db,null,null);

            cursorNew.moveToFirst();

            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "Account");
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, SUBTITLE_FOR_ACCOUNT);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_ID, Integer.toString(account.getAccountID()));
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ, SerializationOperations.serializeObjectToBtyeArray(account));

            String selectionNew = FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_ID + " = ?";
            String[] selectionArgsNew = {Integer.toString(account.getAccountID())};
            Log.i("Database", Integer.toString(account.getAccountID()) + "---" + selectionArgsNew[0]);

            int xx = db.update(FeedReaderContract.FeedEntry.TABLE_NAME, values, selectionNew, selectionArgsNew); // change

            cursorNew.moveToFirst();
            cursorNew.close();

        }

        String selection1 = FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " = ?";
        String[] selectionArgs1 = {SUBTITLE_FOR_ACCOUNT};

        Cursor cursor3 = this.queryDatabase(db,selection1,selectionArgs1);

        cursor3.moveToFirst();

        Log.i("Database", Integer.toString(cursor3.getCount()));
        Account cc = (Account) SerializationOperations.deserializeToObject(cursor3.getBlob(cursor3.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ)));

        Log.i("Database", "Row Id : " + cursor3.getString(cursor3.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE)));
        Log.i("Database", " The request account to be logged in  : \n\n\n" + cc.toString() + "\n\n");
        Log.i("Database", " The current logged in account : \n" + cc.toString());

        cursor.close();
        cursor3.close();
        db.close();

    }

    /**
     * Removes logged in subtitle form account in database
     * @param account
     * @param context
     * @return
     */
    public boolean unCommitAccountToDatabase(Account account, Context context)
    {
        SQLiteDatabase db = getWritableDatabase(context);
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " = ?";
        String[] selectionArgs = {SUBTITLE_FOR_ACCOUNT};

        ContentValues cv = new ContentValues();
        cv.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "Subtitle");

        if (db != null)
        {
            if (this.updateRow(db,cv, selection,selectionArgs))
            {
                return true;
            }

        }
        if (db.isOpen()) {
            db.close();
        }
        return  false;
    }


    public Account getLoggedInAccountFromDatabase(Context context) {
        Log.i("Database", "Account backup retrieved from database");
        Account c = null;
        SQLiteDatabase db = AccountDatabaseInterface.getInstance().getWritableDatabase(context);
        String selection1 = FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " = ?";
        String[] selectionArgs1 = {SUBTITLE_FOR_ACCOUNT};

        Cursor cursor = this.queryDatabase(db,selection1,selectionArgs1);
        if (cursor.getCount() >0) {

            cursor.moveToFirst();
            c = (Account) SerializationOperations.deserializeToObject(cursor.getBlob(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ)));
            if (c != null) {
                Log.i("Database", " The request account to be logged in  : \n\n\n" + c.toString() + "\n\n");
                Log.i("Database", " The current logged in account : \n" + c.toString());
            }

            db.close();
            cursor.close();
        }
        else
        {

        }


        if (db.isOpen())
        {
            if (cursor.isClosed() == false)
            {
                cursor.close();
            }
            db.close();
        }
        return c;
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


            Account c = null;
            c = (Account) SerializationOperations.deserializeToObject(cursor.getBlob(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ)));
            if (c != null) {
                Log.i(this.getClass().getName(), "Account read from database");

                accounts.add(c);
                Log.i("Mine", "From Data base : " + accounts.get(accounts.size() - 1));
            }
            else
            {
                Log.i(this.getClass().getName(), "Null account qued for deletion");
                String id = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry._ID));
                String selectionDelete = FeedReaderContract.FeedEntry._ID + " = ?";
                String[] selectionArgsDelete = {id};
                this.deleteRow(db,selectionDelete,selectionArgsDelete);
            }


            while (cursor.moveToNext()) {

                c = null;
               c = (Account) SerializationOperations.deserializeToObject(cursor.getBlob(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ)));
                if (c != null) {
                    Log.i(this.getClass().getName(), "Account read from database");

                    accounts.add(c);
                }
                else
                {
                    Log.i(this.getClass().getName(), "Null account qued for deletion");
                    String id = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry._ID));
                    String selectionDelete = FeedReaderContract.FeedEntry._ID + " = ?";
                    String[] selectionArgsDelete = {id};
                    this.deleteRow(db,selectionDelete,selectionArgsDelete);
                }

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


        db.close();
        cursor.close();
        return returnAccounts;
    }


    public boolean updateAccountToDatabase(Context context, Account account)
    {
        Log.i(this.getClass().getName(), "update account called");
            String rowNum;
            // Filter results WHERE "title" = 'My Title'
            String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ACCOUNT_ID + " = ?";
            String[] selectionArgs = {Integer.toString(account.getAccountID())};


            String sortOrder =
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

            SQLiteDatabase db = AccountDatabaseInterface.getInstance().getWritableDatabase(context);
            Cursor cursor = this.queryDatabase(db, null, null);
            if (cursor == null)
            {
                Log.i(this.getClass().getName(), "cursor null at line 257");
            }
        try
        {


            ContentValues cv = new ContentValues();
            cv.put(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJ, SerializationOperations.serializeObjectToBtyeArray(account));

            if (cursor.moveToFirst()) {
                if (cursor.getCount() == 1) {

                    rowNum = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                    String[] x1 = {rowNum};
                    if (this.updateRow(db, cv, FeedReaderContract.FeedEntry._ID + "= ?", x1)) {
                        Log.i(this.getClass().getName(), "update account completed");
                        return true;
                    }
                    else
                    {
                        Log.i(this.getClass().getName(), "bad selection args to update line 271");
                    }

                } else if (cursor.getCount() == 0) {
                    // account not saved
                    Log.i(this.getClass().getName(), "no account exists line 272");

                } else if (cursor.getCount() > 1) {
                    // account recorded twice
                    Log.i(this.getClass().getName(), "two accounts of the same exists in database");
                } else {
                    // cursor is null
                    Log.i(this.getClass().getName(), "Cursor is null when saving account line 277");
                }
            }
        }
        catch(Exception e)
        {
            Log.i(getClass().getName(), e.getMessage());
        }
        finally {
            db.close();
            cursor.close();

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
       db.close();

       if (x > 0)
       {
           return true;
       }

        return  false;
    }

    public boolean updateRow(SQLiteDatabase db, ContentValues cv,  String selection, String[] selectionArgs)
    {


        int xx = db.update(FeedReaderContract.FeedEntry.TABLE_NAME, cv, selection, selectionArgs);
        db.close();
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
