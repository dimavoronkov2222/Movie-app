package com.dmytro.moviesapp;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class MovieDetailsActivity extends AppCompatActivity {
    private TextView textTitle, textDescription, textGenre, textDirector, textProducer, textStudio;
    private DatabaseHelper databaseHelper;
    private int movieId;
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
        databaseHelper = new DatabaseHelper(this);
        movieId = getIntent().getIntExtra("MOVIE_ID", -1);
        if (movieId != -1) {
            loadMovieDetails();
        }
    }
    private void loadMovieDetails() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM movies WHERE id = ?", new String[]{String.valueOf(movieId)});
        if (cursor.moveToFirst()) {
            textTitle.setText(cursor.getString(1));
            textDescription.setText(cursor.getString(2));
            textGenre.setText("Жанр: " + cursor.getString(3));
            textDirector.setText("Режисер: " + cursor.getString(4));
            textProducer.setText("Продюсер: " + cursor.getString(5));
            textStudio.setText("Студія: " + cursor.getString(6));
        }
        cursor.close();
        db.close();
    }
}