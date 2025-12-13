package org.example.movielibraryapi.services.contracts;

import java.util.concurrent.CompletableFuture;

public interface MovieRatingEnrichmentService {

    CompletableFuture<Void> enrichRating(Long movieId, String title);
}
