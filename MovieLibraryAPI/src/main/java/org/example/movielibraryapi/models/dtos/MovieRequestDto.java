package org.example.movielibraryapi.models.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequestDto {

    private static final String TITLE_REQUIRED = "Title is required";
    private static final String TITLE_LIMIT = "Title cannot exceed 255 characters";

    private static final String RELEASE_YEAR_REQUIRED = "Release year is required";
    private static final String RELEASE_YEAR_LOWER_BOUND = "Release year cannot be before 1888";
    private static final String RELEASE_YEAR_UPPER_BOUND = "Release year cannot be in the future";

    public static final String DIRECTOR_REQUIRED = "Director is required";
    public static final String DIRECTOR_LIMIT = "Director name cannot exceed 255 characters";




    @NotBlank(message = TITLE_REQUIRED)
    @Size(max = 255, message = TITLE_LIMIT)
    private String title;

    @NotBlank(message = DIRECTOR_REQUIRED)
    @Size(max = 255, message = DIRECTOR_LIMIT)
    private String director;

    @NotNull(message = RELEASE_YEAR_REQUIRED)
    @Min(value = 1888, message = RELEASE_YEAR_LOWER_BOUND)
    @Max(value = 2100, message = RELEASE_YEAR_UPPER_BOUND)
    private Integer releaseYear;
}
