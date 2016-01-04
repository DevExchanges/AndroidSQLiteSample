package info.devexchanges.androidsqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myFriendDB";

    // Friend table name
    private static final String TABLE_FRIEND = "friend";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_JOB = "job";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FRIEND_TABLE = "CREATE TABLE " + TABLE_FRIEND + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_JOB + " TEXT" + ")";
        db.execSQL(CREATE_FRIEND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);

        // Create tables again
        onCreate(db);
    }

    // Adding a new record (friend) to table
    void addNewFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, friend.getName());
        values.put(KEY_JOB, friend.getJob());

        // inserting this record
        db.insert(TABLE_FRIEND, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Friends in Table of Database
    public List<Friend> getAllFriends() {
        List<Friend> friendList = new ArrayList<>();

        // select query
        String selectQuery = "SELECT  * FROM " + TABLE_FRIEND;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all table records and adding to list
        if (cursor.moveToFirst()) {
            do {
                Friend friend = new Friend();
                friend.setId(Integer.parseInt(cursor.getString(0)));
                friend.setName(cursor.getString(1));
                friend.setJob(cursor.getString(2));

                // Adding friend to list
                friendList.add(friend);
            } while (cursor.moveToNext());
        }

        return friendList;
    }

    // Updating a record in database table
    public int updateFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, friend.getName());
        values.put(KEY_JOB, friend.getJob());

        // updating row
        return db.update(TABLE_FRIEND, values, KEY_ID + " = ?", new String[]{String.valueOf(friend.getId())});
    }

    // Deleting a record in database table
    public void deleteFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIEND, KEY_ID + " = ?", new String[]{String.valueOf(friend.getId())});
        db.close();
    }

    // getting number of records in table
    public int getContactsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor dataCount = db.rawQuery("select " + KEY_ID + " from " + TABLE_FRIEND, null);

        int count = dataCount.getCount();
        dataCount.close();
        db.close();

        return count;
    }
}