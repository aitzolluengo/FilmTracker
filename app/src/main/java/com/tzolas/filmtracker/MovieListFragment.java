package com.tzolas.filmtracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tzolas.filmtracker.adapters.MovieAdapter;
import com.tzolas.filmtracker.entities.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadMovies(); // Cargar las películas

        return view;
    }

    private void loadMovies() {
        // Cargar las películas desde la base de datos o cualquier fuente
        movieList.add(new Movie("Inception", "Christopher Nolan", "Ciencia Ficcion", 2010, 9));
        movieList.add(new Movie("The Dark Knight", "Christopher Nolan", "Accion", 2008, 8));

        movieAdapter = new MovieAdapter(movieList, getContext());
        recyclerView.setAdapter(movieAdapter);
    }
}
