package com.dmytro.moviesapp;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.*;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription, editTextDirector, editTextProducer, editTextStudio;
    private Spinner spinnerGenre;
    private ArrayList<String> movieList;
    private ArrayAdapter<String> movieAdapter;
    @SuppressLint("MissingInflatedId")
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
        Button buttonAddMovie = findViewById(R.id.buttonAddMovie);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonLoad = findViewById(R.id.buttonLoad);
        ListView listViewMovies = findViewById(R.id.listViewMovies);
        movieList = new ArrayList<>();
        movieAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movieList);
        listViewMovies.setAdapter(movieAdapter);
        String[] genres = {"Драма", "Комедия", "Боевик", "Фантастика", "Хоррор"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genres);
        spinnerGenre.setAdapter(adapter);
        buttonAddMovie.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String director = editTextDirector.getText().toString().trim();
            String producer = editTextProducer.getText().toString().trim();
            String studio = editTextStudio.getText().toString().trim();
            String genre = spinnerGenre.getSelectedItem().toString();
            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Заполните все обязательные поля!", Toast.LENGTH_SHORT).show();
                return;
            }
            String movieDetails = title + " (" + genre + ")\nОписание: " + description + "\nРежиссер: " + director +
                    "\nПродюсер: " + producer + "\nСтудия: " + studio;
            movieList.add(movieDetails);
            movieAdapter.notifyDataSetChanged();
            editTextTitle.setText("");
            editTextDescription.setText("");
            editTextDirector.setText("");
            editTextProducer.setText("");
            editTextStudio.setText("");
            Toast.makeText(this, "Фильм добавлен!", Toast.LENGTH_SHORT).show();
        });
        listViewMovies.setOnItemClickListener((parent, view, position, id) -> {
            movieList.remove(position);
            movieAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Фильм удален", Toast.LENGTH_SHORT).show();
        });
        buttonSave.setOnClickListener(v -> saveToFile());
        buttonLoad.setOnClickListener(v -> loadFromFile());
    }
    private void saveToFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "movies.json");
        saveFileLauncher.launch(intent);
    }
    private final ActivityResultLauncher<Intent> saveFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                        if (outputStream != null) {
                            JSONArray jsonArray = new JSONArray(movieList);
                            outputStream.write(jsonArray.toString().getBytes());
                            outputStream.close();
                            Toast.makeText(this, "Список сохранен в JSON", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private void loadFromFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/json");
        loadFileLauncher.launch(intent);
    }
    private final ActivityResultLauncher<Intent> loadFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    try (InputStream inputStream = getContentResolver().openInputStream(uri);
                         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                        StringBuilder jsonString = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            jsonString.append(line);
                        }
                        movieList.clear();
                        JSONArray jsonArray = new JSONArray(jsonString.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            movieList.add(jsonArray.getString(i));
                        }
                        movieAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Список загружен из JSON", Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}