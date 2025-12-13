package org.example.movielibraryapi.controllers;

import jakarta.validation.Valid;
import org.example.movielibraryapi.exceptions.DuplicateEntityException;
import org.example.movielibraryapi.exceptions.EntityNotFoundException;
import org.example.movielibraryapi.helpers.ModelMapper;
import org.example.movielibraryapi.models.Movie;
import org.example.movielibraryapi.models.dtos.MovieRequestDto;
import org.example.movielibraryapi.models.dtos.MovieResponseDto;
import org.example.movielibraryapi.services.contracts.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final ModelMapper modelMapper;

    @Autowired
    public MovieController(MovieService movieService, ModelMapper modelMapper) {
        this.movieService = movieService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public MovieResponseDto createMovie(@RequestBody @Valid MovieRequestDto dto) {
        try {
            Movie movie = modelMapper.toEntity(dto);
            return modelMapper.toDto(movieService.create(movie));
        } catch (DuplicateEntityException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping
    public List<MovieResponseDto> getAllMovies() {
        return movieService.getAll().stream()
                .map(modelMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public MovieResponseDto getMovieById(@PathVariable Long id) {
        try {
            Movie movie = movieService.getById(id);
            return modelMapper.toDto(movie);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/title/{title}")
    public MovieResponseDto getByTitle(@PathVariable String title) {
        try {
            Movie movie = movieService.getByTitle(title);
            return modelMapper.toDto(movie);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public MovieResponseDto updateMovie(@PathVariable Long id, @Valid @RequestBody MovieRequestDto dto) {
        try{
            Movie movie = modelMapper.toEntity(dto);
            return modelMapper.toDto(movieService.update(id,movie));
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        try{
            movieService.delete(id);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
