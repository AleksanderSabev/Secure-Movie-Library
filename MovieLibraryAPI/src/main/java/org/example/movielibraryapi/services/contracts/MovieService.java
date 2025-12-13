package org.example.movielibraryapi.services.contracts;

import org.example.movielibraryapi.models.Movie;

import java.util.List;

public interface MovieService {

    Movie create(Movie movie);

    List<Movie> getAll();

    Movie getByTitle(String title);

    Movie getById(Long id);

    Movie update(Long id, Movie movie);

    void delete(Long id);
}
