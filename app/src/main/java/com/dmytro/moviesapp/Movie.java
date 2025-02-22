package com.dmytro.moviesapp;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    public String genre;
    public String director;
    public String producer;
    public String studio;
}