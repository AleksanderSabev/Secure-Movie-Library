package org.example.movielibraryapi.controllers;

import jakarta.validation.Valid;
import org.example.movielibraryapi.models.dtos.MovieRequestDTO;
import org.example.movielibraryapi.models.dtos.MovieResponseDTO;
import org.example.movielibraryapi.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/movies")
    public MovieResponseDTO createMovie(@RequestBody @Valid MovieRequestDTO dto) { return null; }

    @GetMapping("/movies")
    public List<MovieResponseDTO> getAllMovies() { return null; }

    @GetMapping("/movies/{id}")
    public MovieResponseDTO getMovie(@PathVariable Long id) { return null; }

    @PutMapping("/movies/{id}")
    public MovieResponseDTO updateMovie(@PathVariable Long id, @RequestBody MovieRequestDTO dto) { return null; }

    @DeleteMapping("/movies/{id}")
    public void deleteMovie(@PathVariable Long id) {}
}
