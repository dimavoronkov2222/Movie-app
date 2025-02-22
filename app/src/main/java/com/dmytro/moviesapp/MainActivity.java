package com.dmytro.moviesapp;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription, editTextDirector, editTextProducer, editTextStudio;
    private Spinner spinnerGenre;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> movieAdapter;
    private ArrayList<String> movieList;
    private ArrayList<Integer> movieIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDirector = findViewById(R.id.editTextDirector);
        editTextProducer = findViewById(R.id.editTextProducer);
        editTextStudio = findViewById(R.id.editTextStudio);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        ListView listViewMovies = findViewById(R.id.listViewMovies);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        databaseHelper = new DatabaseHelper(this);
        movieList = new ArrayList<>();
        movieIds = new ArrayList<>();
        movieAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movieList);
        listViewMovies.setAdapter(movieAdapter);
        loadGenresIntoSpinner();
        buttonAdd.setOnClickListener(v -> addMovie());
        listViewMovies.setOnItemClickListener((parent, view, position, id) -> openMovieDetails(movieIds.get(position)));
        listViewMovies.setOnItemLongClickListener((parent, view, position, id) -> {
            int movieIdToDelete = movieIds.get(position);
            databaseHelper.deleteMovie(movieIdToDelete);
            loadMovies();
            return true;
        });
        loadMovies();
    }
    private void addMovie() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String genre = spinnerGenre.getSelectedItem().toString();
        String director = editTextDirector.getText().toString().trim();
        String producer = editTextProducer.getText().toString().trim();
        String studio = editTextStudio.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(this, "Введіть назву фільму", Toast.LENGTH_SHORT).show();
            return;
        }
        int genreId = getGenreId(genre);
        databaseHelper.addMovie(title, description, genreId, director, producer, studio);
        loadMovies();
        editTextTitle.setText("");
        editTextDescription.setText("");
        editTextDirector.setText("");
        editTextProducer.setText("");
        editTextStudio.setText("");
    }
    private void loadMovies() {
        movieList.clear();
        movieIds.clear();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, title, categories.name AS genre FROM movies INNER JOIN categories ON movies.genre_id = categories.id", null);
        while (cursor.moveToNext()) {
            movieIds.add(cursor.getInt(0));
            movieList.add(cursor.getString(1) + " (" + cursor.getString(2) + ")");
        }
        cursor.close();
        db.close();
        movieAdapter.notifyDataSetChanged();
    }
    private void openMovieDetails(int movieId) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("MOVIE_ID", movieId);
        startActivity(intent);
    }
    private int getGenreId(String genreName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM categories WHERE name = ?", new String[]{genreName});
        int genreId = -1;
        if (cursor != null && cursor.moveToFirst()) {
            genreId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return genreId;
    }
    private void loadGenresIntoSpinner() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM categories", null);
        ArrayList<String> genres = new ArrayList<>();
        while (cursor.moveToNext()) {
            genres.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genres);
        spinnerGenre.setAdapter(genreAdapter);
    }
}