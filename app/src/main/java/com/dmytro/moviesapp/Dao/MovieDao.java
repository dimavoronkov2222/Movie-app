package com.dmytro.moviesapp.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.dmytro.moviesapp.odt.Movie;
import java.util.List;
@Dao
public interface MovieDao {
    @Insert
    void insert(Movie movie);
    @Update
    void update(Movie movie);
    @Delete
    void delete(Movie movie);
    @Query("SELECT * FROM Movie WHERE categoryId = :categoryId")
    List<Movie> getMoviesByCategory(int categoryId);
    Movie getMovieById(Integer param);
    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAllMovies();
}