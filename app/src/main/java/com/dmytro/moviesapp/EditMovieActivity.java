package com.dmytro.moviesapp;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
public class EditMovieActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription, editTextDirector, editTextProducer, editTextStudio;
    private Spinner spinnerGenre;
    private DatabaseHelper databaseHelper;
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
        databaseHelper = new DatabaseHelper(this);
        movieId = getIntent().getIntExtra("MOVIE_ID", -1);
        if (movieId != -1) {
            loadMovieDetails();
        }
        buttonSave.setOnClickListener(v -> saveMovie());
    }
    private void loadMovieDetails() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM movies WHERE id = ?", new String[]{String.valueOf(movieId)});
        if (cursor.moveToFirst()) {
            editTextTitle.setText(cursor.getString(1));
            editTextDescription.setText(cursor.getString(2));
            spinnerGenre.setSelection(((ArrayAdapter) spinnerGenre.getAdapter()).getPosition(cursor.getString(3)));
            editTextDirector.setText(cursor.getString(4));
            editTextProducer.setText(cursor.getString(5));
            editTextStudio.setText(cursor.getString(6));
        }
        cursor.close();
        db.close();
    }
    private void saveMovie() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String genre = spinnerGenre.getSelectedItem().toString();
        String director = editTextDirector.getText().toString().trim();
        String producer = editTextProducer.getText().toString().trim();
        String studio = editTextStudio.getText().toString().trim();
        databaseHelper.updateMovie(movieId, title, description, genre, director, producer, studio);
        finish();
    }
}