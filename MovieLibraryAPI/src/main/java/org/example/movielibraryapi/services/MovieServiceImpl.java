package org.example.movielibraryapi.services;

import org.example.movielibraryapi.models.dtos.MovieRequestDTO;
import org.example.movielibraryapi.models.dtos.MovieResponseDTO;
import org.example.movielibraryapi.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{

    private MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public MovieResponseDTO create(MovieRequestDTO dto) {
        return null;
    }

    @Override
    public List<MovieResponseDTO> findAll() {
        return List.of();
    }

    @Override
    public MovieResponseDTO findById(Long id) {
        return null;
    }

    @Override
    public MovieResponseDTO update(Long id, MovieRequestDTO dto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
