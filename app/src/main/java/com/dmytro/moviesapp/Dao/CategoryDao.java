package com.dmytro.moviesapp.dao;
import androidx.room.*;
import com.dmytro.moviesapp.odt.Category;
import java.util.List;
@Dao
public interface CategoryDao {
    @Insert
    void insert(Category category);
    @Update
    void update(Category category);
    @Delete
    void delete(Category category);
    @Query("SELECT * FROM Category")
    List<Category> getAllCategories();
}