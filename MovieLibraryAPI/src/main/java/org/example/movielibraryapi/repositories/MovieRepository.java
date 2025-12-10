package org.example.movielibraryapi.repositories;

import org.example.movielibraryapi.models.Movie;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<@NonNull Movie, @NonNull Long> {

    Optional<Movie> findByTitle(String title);


}
