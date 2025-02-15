package com.dmytro.moviesapp;
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
    private EditText editTextTitle;
    private Spinner spinnerGenre;
    private ArrayList<String> movieList;
    private ArrayAdapter<String> movieAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextTitle = findViewById(R.id.editTextTitle);
        spinnerGenre = findViewById(R.id.spinnerGenre);
        Button buttonAdd = findViewById(R.id.buttonAdd);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonLoad = findViewById(R.id.buttonLoad);
        ListView listViewMovies = findViewById(R.id.listViewMovies);
        String[] genres = {"Драма", "Комедия", "Боевик", "Фантастика", "Хоррор"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genres);
        spinnerGenre.setAdapter(adapter);
        movieList = new ArrayList<>();
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
            editTextTitle.setText("");
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
