package com.dmytro.moviesapp.database;
import android.content.Context;
import com.dmytro.moviesapp.odt.Movie;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.dmytro.moviesapp.odt.Category;
import com.dmytro.moviesapp.dao.CategoryDao;
import com.dmytro.moviesapp.dao.MovieDao;
@Database(entities = {Category.class, Movie.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;
    public abstract CategoryDao categoryDao();
    public abstract MovieDao movieDao();
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}