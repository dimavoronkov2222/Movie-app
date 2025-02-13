package com.dmytro.moviesapp;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
public class MainActivity extends Activity {
    private EditText editTextTitle;
    private Spinner spinnerGenre;
    private ArrayList<String> movieList;
    private ArrayAdapter<String> movieAdapter;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MoviesPref";
    private static final String MOVIES_KEY = "movies";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextTitle = findViewById(R.id.editTextTitle);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        ListView listViewMovies = findViewById(R.id.listViewMovies);
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String[] genres = {"Драма", "Комедия", "Боевик", "Фантастика", "Хоррор"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genres);
        spinnerGenre.setAdapter(adapter);
        movieList = loadMovies();
        movieAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movieList);
        listViewMovies.setAdapter(movieAdapter);
        buttonAdd.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString().trim();
            String selectedGenre = spinnerGenre.getSelectedItem().toString();
            if (title.isEmpty()) {
                Toast.makeText(this, "Введите название фильма", Toast.LENGTH_SHORT).show();
                return;
            }
            String movieEntry = title + " (" + selectedGenre + ")";
            movieList.add(movieEntry);
            movieAdapter.notifyDataSetChanged();
            saveMovies();
            editTextTitle.setText("");
            Toast.makeText(this, "Фильм добавлен!", Toast.LENGTH_SHORT).show();
        });
        listViewMovies.setOnItemClickListener((parent, view, position, id) -> {
            movieList.remove(position);
            movieAdapter.notifyDataSetChanged();
            saveMovies();
            Toast.makeText(this, "Фильм удален", Toast.LENGTH_SHORT).show();
        });
    }
    private void saveMovies() {
        JSONArray jsonArray = new JSONArray();
        for (String movie : movieList) {
            jsonArray.put(movie);
        }
        sharedPreferences.edit().putString(MOVIES_KEY, jsonArray.toString()).apply();
    }
    private ArrayList<String> loadMovies() {
        ArrayList<String> movies = new ArrayList<>();
        String jsonString = sharedPreferences.getString(MOVIES_KEY, "[]");
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                movies.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }
}