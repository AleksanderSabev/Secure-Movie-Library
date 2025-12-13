package org.example.movielibraryapi.helpers;

import org.example.movielibraryapi.models.Movie;
import org.example.movielibraryapi.models.dtos.MovieRequestDto;
import org.example.movielibraryapi.models.dtos.MovieResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    public Movie toEntity(MovieRequestDto dto) {
        if (dto == null) return null;

        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setDirector(dto.getDirector());
        movie.setReleaseYear(dto.getReleaseYear());
        return movie;
    }

    public MovieResponseDto toDto(Movie movie) {
        if (movie == null) return null;

        return new MovieResponseDto(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseYear(),
                movie.getRating()
        );
    }


    public void updateEntity(Movie movie, MovieRequestDto dto) {
        if (dto.getTitle() != null) movie.setTitle(dto.getTitle());
        if (dto.getDirector() != null) movie.setDirector(dto.getDirector());
        if (dto.getReleaseYear() != null) movie.setReleaseYear(dto.getReleaseYear());
    }
}
