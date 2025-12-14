package org.example.movielibraryapi.services;

import org.example.movielibraryapi.exceptions.DuplicateEntityException;
import org.example.movielibraryapi.exceptions.EntityNotFoundException;
import org.example.movielibraryapi.models.Movie;
import org.example.movielibraryapi.repositories.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl service;

    @Test
    void create_shouldSaveMovie_whenNotDuplicate() {
        Movie movie = new Movie();
        movie.setTitle("Inception");

        when(movieRepository.existsByTitle("Inception")).thenReturn(false);
        when(movieRepository.save(movie)).thenReturn(movie);

        Movie saved = service.create(movie);

        assertEquals("Inception", saved.getTitle());
        verify(movieRepository).save(movie);
    }

    @Test
    void create_shouldThrowDuplicateEntityException_whenTitleExists() {
        Movie movie = new Movie();
        movie.setTitle("Inception");

        when(movieRepository.existsByTitle("Inception")).thenReturn(true);

        DuplicateEntityException ex = assertThrows(DuplicateEntityException.class,
                () -> service.create(movie));
        assertTrue(ex.getMessage().contains("title"));
        verify(movieRepository, never()).save(any());
    }

    @Test
    void getAll_shouldReturnAllMovies() {
        List<Movie> movies = List.of(new Movie(), new Movie());
        when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> result = service.getAll();

        assertEquals(2, result.size());
        verify(movieRepository).findAll();
    }

    @Test
    void getByTitle_shouldReturnMovie_whenExists() {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        when(movieRepository.findByTitle("Inception")).thenReturn(Optional.of(movie));

        Movie result = service.getByTitle("Inception");

        assertEquals("Inception", result.getTitle());
    }

    @Test
    void getByTitle_shouldThrowEntityNotFoundException_whenNotExists() {
        when(movieRepository.findByTitle("Inception")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.getByTitle("Inception"));
    }

    @Test
    void getById_shouldReturnMovie_whenExists() {
        Movie movie = new Movie();
        movie.setId(1L);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Movie result = service.getById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getById_shouldThrowEntityNotFoundException_whenNotExists() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.getById(1L));
    }

    @Test
    void update_shouldModifyExistingMovie() {
        Movie movie = new Movie();
        movie.setTitle("New Title");
        movie.setReleaseYear(2020);
        movie.setRating(8.5);

        Movie existing = new Movie();
        existing.setId(1L);
        existing.setTitle("Old Title");
        existing.setReleaseYear(2000);
        existing.setRating(5.0);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(existing));

        Movie updated = service.update(1L, movie);

        assertEquals("New Title", updated.getTitle());
        assertEquals(2020, updated.getReleaseYear());
        assertEquals(8.5, updated.getRating());
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenMovieNotExists() {
        Movie movie = new Movie();
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(1L, movie));
    }

    @Test
    void updateRating_shouldSetRating_whenMovieExists() {
        Movie movie = new Movie();
        movie.setId(1L);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        service.updateRating(1L, 9.2);

        assertEquals(9.2, movie.getRating());
    }

    @Test
    void updateRating_shouldThrowEntityNotFoundException_whenMovieNotExists() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updateRating(1L, 8.0));
    }

    @Test
    void delete_shouldCallRepository_whenMovieExists() {
        when(movieRepository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(movieRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowEntityNotFoundException_whenMovieNotExists() {
        when(movieRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.delete(1L));
    }
}
