package com.tzolas.filmtracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tzolas.filmtracker.R;
import com.tzolas.filmtracker.entities.Movie;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context context;
    private final OnMovieClickListener movieClickListener;
    private final OnMovieDeleteListener movieDeleteListener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public interface OnMovieDeleteListener {
        void onMovieDelete(Movie movie);
    }

    public MovieAdapter(List<Movie> movieList, Context context,
                        OnMovieClickListener movieClickListener,
                        OnMovieDeleteListener movieDeleteListener) {
        this.movieList = movieList;
        this.context = context;
        this.movieClickListener = movieClickListener;
        this.movieDeleteListener = movieDeleteListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.year.setText(String.valueOf(movie.getYear()));
        holder.rating.setText(String.valueOf(movie.getRating()));

        holder.itemView.setOnClickListener(v -> movieClickListener.onMovieClick(movie));
        holder.itemView.setOnLongClickListener(v -> {
            movieDeleteListener.onMovieDelete(movie);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void updateMovies(List<Movie> newMovies) {
        this.movieList = newMovies;
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title, genre, year, rating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movieTitle);
            genre = itemView.findViewById(R.id.movieGenre);
            year = itemView.findViewById(R.id.movieYear);
            rating = itemView.findViewById(R.id.movieRating);
        }
    }
}
