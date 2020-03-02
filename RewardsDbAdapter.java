package com.cis2237.bingp4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class RewardsDbAdapter {

    // set up for Singleton Pattern
    private static RewardsDbAdapter INSTANCE = null;

    // Columns
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_FREQUENTFLYER = "frequentflyer";
    public static final String COL_MILES = "miles";

    // Indicies
    public static final int INDEX_ID = 0;
    public static final int INDEX_NAME = INDEX_ID + 1;
    public static final int INDEX_FREQUENTFLYER = INDEX_ID + 2;
    public static final int INDEX_MILES = INDEX_ID + 2;

    // Logging parameters
    private static final String TAG = "RewardsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "rewards";
    private static final String TABLE_NAME = "tbl_subscriber";
    private static final int DATABASE_VERSION = 1;
    private static Context mCtx = null;

    // SQLLite stuff
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_NAME + " TEXT, " +
                    COL_FREQUENTFLYER + " TEXT, " +
                    COL_MILES + " INTEGER )";


    // Constructor
    public RewardsDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /*public Context getmCtx() {
        return mCtx;
    }*/

    // static method to create instance of Singleton class
    public static RewardsDbAdapter getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new RewardsDbAdapter(mCtx);

        return INSTANCE;
    }

    // Open the database
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }

    // Close the database
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    // Make a new subscriber
    public void createSubscriber(String name, String frequentFlyer, int  miles) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_FREQUENTFLYER, frequentFlyer);
        values.put(COL_MILES, miles);
        mDb.insert(TABLE_NAME, null, values);
    }

    //  Same as above but includes subscriber
    public long createSubscriber(Subscriber subscriber) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, subscriber.getName()); // Contact Name
        values.put(COL_FREQUENTFLYER, subscriber.getFrequentFlyer());
        values.put(COL_MILES, subscriber.getMiles());

        // Inserting Row
        return mDb.insert(TABLE_NAME, null, values);
    }

    //  Fetch a single subscriber
    public Subscriber fetchSubscriberById(int id) {

        Cursor cursor = mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_NAME, COL_FREQUENTFLYER, COL_MILES}, COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null
        );
        if (cursor != null)
            cursor.moveToFirst();
        return new Subscriber(
                cursor.getInt(INDEX_ID),
                cursor.getString(INDEX_NAME),
                cursor.getString(INDEX_FREQUENTFLYER),
                cursor.getInt(INDEX_MILES)
        );

    }

    // Get all subscribers
    public Cursor fetchAllRewards() {
        Cursor mCursor = mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_NAME, COL_FREQUENTFLYER, COL_MILES },
                null, null, null, null, null
        );
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //  Edit a subscriber
    public void updateSubscriber(Subscriber subscriber) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, subscriber.getName());
        values.put(COL_FREQUENTFLYER, subscriber.getFrequentFlyer());
        values.put(COL_MILES, subscriber.getMiles());
        mDb.update(TABLE_NAME, values,
                COL_ID + "=?", new String[]{String.valueOf(subscriber.getId())});
    }
    // Remove a rminder
    public void deleteSubscriberById(int nId) {
        mDb.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(nId)});
    }
    public void deleteAllSubscribers() {
        mDb.delete(TABLE_NAME, null, null);
    }

    // SQLLITE inner class database helper
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}
