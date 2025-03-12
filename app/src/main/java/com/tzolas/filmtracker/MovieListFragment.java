package com.tzolas.filmtracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tzolas.filmtracker.adapters.MovieAdapter;
import com.tzolas.filmtracker.database.MovieDatabase;
import com.tzolas.filmtracker.entities.Movie;
import java.util.List;

public class MovieListFragment extends Fragment {
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private MovieDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database = MovieDatabase.getInstance(getContext());
        movieList = database.movieDao().getAllMovies();

        adapter = new MovieAdapter(movieList, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
