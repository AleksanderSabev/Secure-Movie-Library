package org.example.movielibraryapi.models.dtos;

public class MovieResponseDto {

    private Long id;
    private String title;
    private Integer releaseYear;
    private Double rating;

    public MovieResponseDto() {}

    public MovieResponseDto(Long id, String title, Integer releaseYear, Double rating) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
}
