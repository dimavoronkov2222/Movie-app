package com.dmytro.moviesapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.dmytro.moviesapp.database.AppDatabase;
import com.dmytro.moviesapp.dao.MovieDao;
import com.dmytro.moviesapp.odt.Category;
import com.dmytro.moviesapp.odt.Movie;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription, editTextDirector, editTextProducer, editTextStudio;
    private Spinner spinnerGenre;
    private MovieDao movieDao;
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
        AppDatabase database = AppDatabase.getInstance(this);
        movieDao = database.movieDao();
        movieList = new ArrayList<>();
        movieIds = new ArrayList<>();
        movieAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movieList);
        listViewMovies.setAdapter(movieAdapter);
        loadGenresIntoSpinner();
        buttonAdd.setOnClickListener(v -> addMovie());
        listViewMovies.setOnItemClickListener((parent, view, position, id) -> openMovieDetails(movieIds.get(position)));
        listViewMovies.setOnItemLongClickListener((parent, view, position, id) -> {
            int movieIdToDelete = movieIds.get(position);
            deleteMovie(movieIdToDelete);
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
        Movie movie = new Movie(title, description, genreId, director, producer, studio);
        movieDao.insert(movie);
        loadMovies();
        clearInputs();
    }
    private void loadMovies() {
        movieList.clear();
        movieIds.clear();
        movieDao.getAllMovies().observe(this, movies -> {
            for (Movie movie : movies) {
                movieIds.add(movie.getId());
                movieList.add(movie.getTitle() + " (" + movie.getGenreName() + ")");
            }
            movieAdapter.notifyDataSetChanged();
        });
    }
    private void openMovieDetails(int movieId) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("MOVIE_ID", movieId);
        startActivity(intent);
    }
    private int getGenreId(String genreName) {
        return spinnerGenre.getSelectedItemPosition();
    }
    private void loadGenresIntoSpinner() {
        AppDatabase database = AppDatabase.getInstance(this);
        ArrayAdapter<Category> genreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, database.categoryDao().getAllCategories());
        spinnerGenre.setAdapter(genreAdapter);
    }
    private void deleteMovie(int movieId) {
        Movie movie = new Movie();
        movie.setId(movieId);
        movieDao.delete(movie);
    }
    private void clearInputs() {
        editTextTitle.setText("");
        editTextDescription.setText("");
        editTextDirector.setText("");
        editTextProducer.setText("");
        editTextStudio.setText("");
    }
}