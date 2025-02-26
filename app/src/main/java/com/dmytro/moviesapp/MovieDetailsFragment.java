package com.dmytro.moviesapp;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
public class MovieDetailsFragment extends Fragment {
    private static final String ARG_MOVIE_TITLE = "movie_title";
    public static MovieDetailsFragment newInstance(String movieTitle) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_TITLE, movieTitle);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        TextView titleTextView = view.findViewById(R.id.movie_title);
        if (getArguments() != null) {
            titleTextView.setText(getArguments().getString(ARG_MOVIE_TITLE));
        }
        return view;
    }
}