package org.example.movielibraryapi.models;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "director", nullable = false)
    private String director;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "rating")
    private Double rating;

    public Movie() {}

    public Movie(String title, String director, int releaseYear) {
        setTitle(title);
        setDirector(director);
        setReleaseYear(releaseYear);
    }

    public Movie(String title, String director, int releaseYear, double rating) {
        this(title,director,releaseYear);
        setRating(rating);
    }

    public void setId(Long id) { this.id = id; }

    public void setTitle(String title) { this.title = title; }

    public void setDirector(String director) { this.director = director; }

    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    public void setRating(double rating) { this.rating = rating; }
}
