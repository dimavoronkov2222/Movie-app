package com.dmytro.moviesapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMoviesTable = "CREATE TABLE movies (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "description TEXT, " +
                "genre TEXT, " +
                "director TEXT, " +
                "producer TEXT, " +
                "studio TEXT)";
        db.execSQL(createMoviesTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS movies");
        onCreate(db);
    }
    public void addMovie(String title, String description, String genre, String director, String producer, String studio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("genre", genre);
        values.put("director", director);
        values.put("producer", producer);
        values.put("studio", studio);
        db.insert("movies", null, values);
        db.close();
    }
    public void updateMovie(int id, String title, String description, String genre, String director, String producer, String studio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("genre", genre);
        values.put("director", director);
        values.put("producer", producer);
        values.put("studio", studio);
        db.update("movies", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void deleteMovie(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("movies", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}