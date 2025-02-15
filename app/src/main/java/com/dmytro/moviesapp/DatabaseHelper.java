package com.dmytro.moviesapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MOVIES = "movies";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_DIRECTOR = "director";
    private static final String COLUMN_PRODUCER = "producer";
    private static final String COLUMN_STUDIO = "studio";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_MOVIES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_GENRE + " TEXT, " +
                COLUMN_DIRECTOR + " TEXT, " +
                COLUMN_PRODUCER + " TEXT, " +
                COLUMN_STUDIO + " TEXT)";
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }
    public boolean addMovie(String title, String description, String genre, String director, String producer, String studio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_GENRE, genre);
        values.put(COLUMN_DIRECTOR, director);
        values.put(COLUMN_PRODUCER, producer);
        values.put(COLUMN_STUDIO, studio);
        long result = db.insert(TABLE_MOVIES, null, values);
        db.close();
        return result != -1;
    }
    public ArrayList<String> getAllMovies() {
        ArrayList<String> movies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MOVIES, null);
        if (cursor.moveToFirst()) {
            do {
                String movie = cursor.getString(1) + " (" + cursor.getString(3) + ")\n" +
                        "Опис: " + cursor.getString(2) + "\n" +
                        "Режисер: " + cursor.getString(4) + "\n" +
                        "Продюсер: " + cursor.getString(5) + "\n" +
                        "Студія: " + cursor.getString(6);
                movies.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return movies;
    }
    public void deleteMovie(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, COLUMN_TITLE + "=?", new String[]{title});
        db.close();
    }
}