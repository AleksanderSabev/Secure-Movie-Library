package org.example.movielibraryapi.models.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class MovieRequestDTO {

    @NotBlank
    private String title;

    private String director;

    @Min(1888) @Max(2100)
    private Integer releaseYear;
}
