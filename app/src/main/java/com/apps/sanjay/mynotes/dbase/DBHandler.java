package com.apps.sanjay.mynotes.dbase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.apps.sanjay.mynotes.model.Note;

import java.util.ArrayList;

/**
 * Created by Sanjay on 5/30/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "my_notes.db";
    private static final String TABLE_NOTES = "notes";
    private static final String COL_ID = "id";
    private static final String COL_NOTE_TITLE = "note_title";
    private static final String COL_NOTE_DESC = "note_desc";
    private static final String COL_NOTE_USER_ID = "note_user_id";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NOTES + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOTE_TITLE + " TEXT, " +
                COL_NOTE_DESC + " TEXT, " +
                COL_NOTE_USER_ID + " INTEGER " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF EXISTS " + TABLE_NOTES);
        onCreate(db);//table deleted so create new

    }

    public void addnote(Note note) {
        ContentValues contentValues = new ContentValues();
        Log.d("add", note.getTitle());
        contentValues.put(COL_NOTE_TITLE, note.getTitle());
        contentValues.put(COL_NOTE_DESC, note.getDescription());
        contentValues.put(COL_NOTE_USER_ID, note.getUser_id());
        SQLiteDatabase db = getWritableDatabase();
        long l = db.insert(TABLE_NOTES, null, contentValues);
        Log.d("ins ",l+"");
        db.close();

    }

    public void deleteNote(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NOTES + " WHERE " + COL_ID + "= ? ;";
        SQLiteStatement stmt = db.compileStatement(query);
        stmt.bindLong(1, id);
        stmt.execute();
        db.close();
        Log.d("delete--- ", id + "");
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_NOTES + " SET " + COL_NOTE_TITLE + "= ? , " +
                COL_NOTE_DESC + " = ?  WHERE " + COL_ID + "= ? ;";
        SQLiteStatement stmt = db.compileStatement(query);
        stmt.bindString(1, note.getTitle());
        stmt.bindString(2, note.getDescription());
        stmt.bindLong(3, note.getId());
        stmt.execute();db.close();
    }

    public ArrayList<Note> getnotes(int user_id) {
        String query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + COL_NOTE_USER_ID + "= " + user_id + ";";

        Cursor crs = getReadableDatabase().rawQuery(query, null);
//        crs.moveToFirst();
        ArrayList<Note> arrayList = new ArrayList<Note>();
        Log.d("size ",arrayList.size()+"");
        while (crs.moveToNext()) {
            Note n = new Note();
            n.setId(crs.getInt(crs.getColumnIndex(COL_ID)));
            n.setTitle(crs.getString(crs.getColumnIndex(COL_NOTE_TITLE)));
            n.setDescription(crs.getString(crs.getColumnIndex(COL_NOTE_DESC)));
            Log.d("Select---", n.getId() + "");
            arrayList.add(n);

        }

        return arrayList;


    }

}
