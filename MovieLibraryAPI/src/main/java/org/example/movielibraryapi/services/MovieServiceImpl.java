package org.example.movielibraryapi.services;

import org.example.movielibraryapi.exceptions.DuplicateEntityException;
import org.example.movielibraryapi.exceptions.EntityNotFoundException;
import org.example.movielibraryapi.models.Movie;
import org.example.movielibraryapi.services.contracts.MovieService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.example.movielibraryapi.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Movie create(Movie movie) {
        if (movieRepository.existsByTitle(movie.getTitle())) {
            throw new DuplicateEntityException("Move", "title", movie.getTitle());
        }

        return movieRepository.save(movie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Movie getByTitle(String title) {
        return movieRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Movie", "title", title));
    }

    @Override
    @Transactional(readOnly = true)
    public Movie getById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie",id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Movie update(Long id, Movie movie) {
        Movie movieToUpdate = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie",id));

        movieToUpdate.setTitle(movie.getTitle());
        movieToUpdate.setReleaseYear(movie.getReleaseYear());
        movieToUpdate.setRating(movie.getRating());

        return movieToUpdate;
    }

    @Override
    @Transactional
    public void updateRating(Long movieId, Double rating) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie", movieId));
        movie.setRating(rating);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Long id) {

        if(!movieRepository.existsById(id)){
            throw new EntityNotFoundException("Movie", id);
        }
        movieRepository.deleteById(id);
    }
}
