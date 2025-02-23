package com.dmytro.moviesapp;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.dmytro.moviesapp.database.AppDatabase;
import com.dmytro.moviesapp.odt.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    private TextView textTitle, textDescription, textGenre, textDirector, textProducer, textStudio;
    private AppDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        textTitle = findViewById(R.id.textTitle);
        textDescription = findViewById(R.id.textDescription);
        textGenre = findViewById(R.id.textGenre);
        textDirector = findViewById(R.id.textDirector);
        textProducer = findViewById(R.id.textProducer);
        textStudio = findViewById(R.id.textStudio);
        database = AppDatabase.getInstance(this);
        int movieId = getIntent().getIntExtra("MOVIE_ID", -1);
        if (movieId != -1) {
            new LoadMovieTask().execute(movieId);
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class LoadMovieTask extends AsyncTask<Integer, Void, Movie> {
        @Override
        protected Movie doInBackground(Integer... params) {
            return database.movieDao().getMovieById(params[0]);
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Movie movie) {
            if (movie != null) {
                textTitle.setText(movie.getTitle());
                textDescription.setText(movie.getDescription());
                textGenre.setText("Жанр: " + movie.getGenre());
                textDirector.setText("Режисер: " + movie.getDirector());
                textProducer.setText("Продюсер: " + movie.getProducer());
                textStudio.setText("Студія: " + movie.getStudio());
            }
        }
    }
}