package com.dmytro.moviesapp.odt;
import static androidx.room.ForeignKey.CASCADE;
import androidx.room.*;
@Entity(foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "id",
        childColumns = "categoryId",
        onDelete = CASCADE))
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String genre;
    private String director;
    private String producer;
    private String studio;
    private int categoryId;
    private int genreId;
    private String genreName;
    public Movie(String description, String genre, int director, String producer, String studio, String categoryId) {
        this.title = String.valueOf(title);
        this.description = description;
        this.genre = genre;
        this.director = String.valueOf(director);
        this.producer = producer;
        this.studio = studio;
        this.categoryId = Integer.parseInt(categoryId);
    }
    public Movie() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public String getProducer() {
        return producer;
    }
    public void setProducer(String producer) {
        this.producer = producer;
    }
    public String getStudio() {
        return studio;
    }
    public void setStudio(String studio) {
        this.studio = studio;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public int getGenreId() {
        return genreId;
    }
    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }
    public String getGenreName() {
        return genreName;
    }
    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}