package org.example.movielibraryapi.services;

import org.example.movielibraryapi.models.dtos.MovieRequestDTO;
import org.example.movielibraryapi.models.dtos.MovieResponseDTO;

import java.util.List;

public interface MovieService {

    MovieResponseDTO create(MovieRequestDTO dto);

    List<MovieResponseDTO> findAll();

    MovieResponseDTO findById(Long id);

    MovieResponseDTO update(Long id, MovieRequestDTO dto);

    void delete(Long id);
}
