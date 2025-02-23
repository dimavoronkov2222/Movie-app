package com.dmytro.moviesapp;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.dmytro.moviesapp.database.AppDatabase;
import com.dmytro.moviesapp.dao.MovieDao;
import com.dmytro.moviesapp.odt.Movie;
public class EditMovieActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription, editTextDirector, editTextProducer, editTextStudio;
    private Spinner spinnerGenre;
    private MovieDao movieDao;
    private int movieId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDirector = findViewById(R.id.editTextDirector);
        editTextProducer = findViewById(R.id.editTextProducer);
        editTextStudio = findViewById(R.id.editTextStudio);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        Button buttonSave = findViewById(R.id.buttonSave);
        AppDatabase database = AppDatabase.getInstance(this);
        movieDao = database.movieDao();
        movieId = getIntent().getIntExtra("MOVIE_ID", -1);
        if (movieId != -1) {
            loadMovieDetails();
        }
        buttonSave.setOnClickListener(v -> saveMovie());
    }
    private void loadMovieDetails() {
        Movie movie = movieDao.getMovieById(movieId);
        if (movie != null) {
            editTextTitle.setText(movie.getTitle());
            editTextDescription.setText(movie.getDescription());
            spinnerGenre.setSelection(movie.getGenreId());
            editTextDirector.setText(movie.getDirector());
            editTextProducer.setText(movie.getProducer());
            editTextStudio.setText(movie.getStudio());
        }
    }
    private void saveMovie() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String genre = spinnerGenre.getSelectedItem().toString();
        String director = editTextDirector.getText().toString().trim();
        String producer = editTextProducer.getText().toString().trim();
        String studio = editTextStudio.getText().toString().trim();
        Movie movie = new Movie(title, description, Integer.parseInt(genre), director, producer, studio);
        movieDao.update(movie);
        finish();
    }
}