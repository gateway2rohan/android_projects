package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class PetDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = PetDbHelper.class.getSimpleName();


    private static final String DATABASE_NAME = "shelter.db";

    private static final int DATABASE_VERSION = 1;

    public PetDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PET_TABLE = "CREATE TABLE " + PetContract.PetEntry.TABLE_NAME + "("
                                + PetContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + PetContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                                + PetContract.PetEntry.COLUMN_PET_BREED + " TEXT, "
                                + PetContract.PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                                + PetContract.PetEntry.COLUMN_PET_WEIGHT + " INETGER NOT NULL DEFAULT 0);";

        //execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_PET_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     * @param i represents oldVersion
     * @param i1 represents newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
