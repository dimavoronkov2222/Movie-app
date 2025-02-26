package com.dmytro.moviesapp;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
public class MovieListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ListView listView = view.findViewById(R.id.movie_list_view);
        String[] movies = {"Inception", "Interstellar", "The Matrix", "Titanic"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, movies);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            MovieDetailsFragment fragment = MovieDetailsFragment.newInstance(movies[position]);
            ((MainActivity) requireActivity()).loadFragment(fragment);
        });
        return view;
    }
}