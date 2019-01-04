package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class PetProvider extends ContentProvider {

    private static final String LOG_TAG = PetProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the pets table */
    private static final int PETS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int PET_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS,PETS);

        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS+"/#",PET_ID);
    }

    private PetDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new PetDbHelper(getContext());
        return true;
    }

    /**
     * @param strings represents projection
     * @param s represents selection
     * @param strings1 represents selectionArgs
     * @param s1 represents sortOrder
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match){
            case PETS:
                cursor = database.query(PetContract.PetEntry.TABLE_NAME,strings,s,strings1,null,null,s1);
                break;

            case PET_ID:
                s = PetContract.PetEntry._ID + "=?";
                //ContentUris.parseId(uri) returns the trailing integer in the content uri which replaces ?
                //in the selection variable
                strings1 = new String[]{ String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(PetContract.PetEntry.TABLE_NAME,strings,s,strings1,null,null,s1);
                break;

            default:
                throw new IllegalArgumentException("cannot query unknown URI " + uri);

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        switch (match){
            case PETS:
                return insertPet(uri,contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPet(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(PetContract.PetEntry.TABLE_NAME,null,contentValues);

        if (id == -1){
            Log.e(LOG_TAG,"Failed to insert row for  " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri,id);
    }

    /**
     * @param s represents selection
     * @param strings represents selectionArgs
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    /**
     * @param s represents selection
     * @param strings represents selectionArgs
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
