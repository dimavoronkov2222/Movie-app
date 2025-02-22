package com.dmytro.moviesapp;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 2;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCategoriesTable = "CREATE TABLE categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL)";
        db.execSQL(createCategoriesTable);
        String createMoviesTable = "CREATE TABLE movies (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "description TEXT, " +
                "genre_id INTEGER, " +
                "director TEXT, " +
                "producer TEXT, " +
                "studio TEXT, " +
                "FOREIGN KEY(genre_id) REFERENCES categories(id))";
        db.execSQL(createMoviesTable);
        db.execSQL("INSERT INTO categories (name) VALUES ('Драма'), ('Комедія'), ('Бойовик'), ('Фантастика'), ('Хорор')");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS categories (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL)");
            db.execSQL("ALTER TABLE movies ADD COLUMN genre_id INTEGER");
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    public void addMovie(String title, String description, int genreId, String director, String producer, String studio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("genre_id", genreId);
        values.put("director", director);
        values.put("producer", producer);
        values.put("studio", studio);
        db.insert("movies", null, values);
        db.close();
    }
    public void updateMovie(int id, String title, String description, int genreId, String director, String producer, String studio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("genre_id", genreId);
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
    public void deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("categories", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public Cursor getAllMovies() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT movies.id, movies.title, movies.description, categories.name AS genre, movies.director, movies.producer, movies.studio " +
                "FROM movies INNER JOIN categories ON movies.genre_id = categories.id", null);
    }
    public Cursor getMovieById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT movies.id, movies.title, movies.description, categories.name AS genre, movies.director, movies.producer, movies.studio " +
                "FROM movies INNER JOIN categories ON movies.genre_id = categories.id WHERE movies.id = ?", new String[]{String.valueOf(id)});
    }
    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM categories", null);
    }
    public void addCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        db.insert("categories", null, values);
        db.close();
    }
    @SuppressLint("Range")
    public String getCategoryNameById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM categories WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("name"));
        }
        return null;
    }
}