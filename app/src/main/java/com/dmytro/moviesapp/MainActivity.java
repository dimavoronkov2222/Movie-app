package com.dmytro.moviesapp;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription, editTextDirector, editTextProducer, editTextStudio;
    private Spinner spinnerGenre;
    private ListView listViewMovies;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> movieAdapter;
    private ArrayList<String> movieList;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDirector = findViewById(R.id.editTextDirector);
        editTextProducer = findViewById(R.id.editTextProducer);
        editTextStudio = findViewById(R.id.editTextStudio);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        Button buttonAddMovie = findViewById(R.id.buttonAddMovie);
        listViewMovies = findViewById(R.id.listViewMovies);
        String[] genres = {"Драма", "Комедія", "Бойовик", "Фантастика", "Хоррор"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genres);
        spinnerGenre.setAdapter(adapter);
        loadMoviesFromDB();
        buttonAddMovie.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String director = editTextDirector.getText().toString().trim();
            String producer = editTextProducer.getText().toString().trim();
            String studio = editTextStudio.getText().toString().trim();
            String genre = spinnerGenre.getSelectedItem().toString();
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Заповніть всі обов'язкові поля!", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean inserted = databaseHelper.addMovie(title, description, genre, director, producer, studio);
            if (inserted) {
                Toast.makeText(this, "Фільм додано!", Toast.LENGTH_SHORT).show();
                loadMoviesFromDB();
            } else {
                Toast.makeText(this, "Помилка додавання!", Toast.LENGTH_SHORT).show();
            }
        });
        listViewMovies.setOnItemClickListener((parent, view, position, id) -> {
            String selectedMovie = movieList.get(position);
            String title = selectedMovie.split(" \\(")[0];
            databaseHelper.deleteMovie(title);
            Toast.makeText(this, "Фільм видалено!", Toast.LENGTH_SHORT).show();
            loadMoviesFromDB();
        });
    }
    private void loadMoviesFromDB() {
        movieList = databaseHelper.getAllMovies();
        movieAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movieList);
        listViewMovies.setAdapter(movieAdapter);
    }
}