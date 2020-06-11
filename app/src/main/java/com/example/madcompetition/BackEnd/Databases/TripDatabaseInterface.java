package com.example.madcompetition.BackEnd.Databases;

import android.content.Context;

class TripDatabaseInterface {
    private static final TripDatabaseInterface ourInstance = new TripDatabaseInterface();

    static TripDatabaseInterface getInstance() {
        return ourInstance;
    }

    private TripDatabaseInterface()
    {

    }

    public void addTripToDatabase(Context context)
    {

    }

    public void removeTripFromDatabase(Context context)
    {

    }

    public void deleteDatabaseEntries(Context context)
    {

    }



}
