package com.apps.sanjay.mynotes.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.apps.sanjay.mynotes.model.Note;
import com.apps.sanjay.mynotes.model.User;

/**
 * Created by Sanjay on 5/30/2016.
 */
public class DBHandlerUser extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "my_notes.db";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_NOTES = "notes";
    private static final String COL_ID = "id";
    private static final String COL_USER_NAME = "note_title";
    private static final String COL_USER_PASS = "note_desc";
    private static final String COL_NOTE_TITLE = "note_title";
    private static final String COL_NOTE_DESC = "note_desc";
    private static final String COL_NOTE_USER_ID = "note_user_id";

    public DBHandlerUser(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_USERS + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NAME + " TEXT, " +
                COL_USER_PASS + " TEXT " +
                ");";
        db.execSQL(query);
        String query1 = "CREATE TABLE " + TABLE_NOTES + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOTE_TITLE + " TEXT, " +
                COL_NOTE_DESC + " TEXT, " +
                COL_NOTE_USER_ID + " INTEGER " +
                ");";
        db.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF EXISTS " + TABLE_USERS);
        onCreate(db);//table deleted so create new

    }

    public int addUser(User user) {
        if (!isUserExist(user.getUsername())) {

            ContentValues contentValues = new ContentValues();
            Log.d("add-- ", user.getUsername());
            contentValues.put(COL_USER_NAME, user.getUsername());
            contentValues.put(COL_USER_PASS, user.getPassword());
            SQLiteDatabase db = getWritableDatabase();
            db.insert(TABLE_USERS, null, contentValues);
            db.close();
            return getUser(user.getUsername(), user.getPassword());
        }
        return -1;


    }




    public int getUser(String uname, String password) {
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USER_NAME + " = ? and " + COL_USER_PASS + " = ?;";
        Cursor crs = getReadableDatabase().rawQuery(query, new String[]{uname, password});
        if (crs.moveToNext()) {
            Log.d("Select user---", crs.getColumnIndex(COL_ID) + "");

            return crs.getInt(crs.getColumnIndex(COL_ID));
        }
        return -1;


    }

    public boolean isUserExist(String uname) {
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USER_NAME + " = ? ;";
        Cursor crs = getReadableDatabase().rawQuery(query, new String[]{uname});
        if (crs.moveToFirst()) {
            Log.d("Exist---", crs.getColumnIndex(COL_ID) + "");
            return true;
        }
        return false;


    }

}
