package org.example.movielibraryapi.services;

import org.example.movielibraryapi.models.dtos.OmdbResponse;
import org.example.movielibraryapi.services.contracts.MovieRatingEnrichmentService;
import org.example.movielibraryapi.services.contracts.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
public class MovieRatingEnrichmentServiceImpl implements MovieRatingEnrichmentService {

    private static final Logger logger = LoggerFactory.getLogger(MovieRatingEnrichmentServiceImpl.class);

    private final MovieService movieService;
    private final WebClient webClient;
    private final String omdbApiKey;

    public MovieRatingEnrichmentServiceImpl(MovieService movieService,
                                      WebClient.Builder webClientBuilder,
                                      @Value("${omdb.api.key}") String omdbApiKey) {
        this.movieService = movieService;
        this.webClient = webClientBuilder.baseUrl("http://www.omdbapi.com/").build();
        this.omdbApiKey = omdbApiKey;
    }

    @Override
    @Async("enrichmentExecutor")
    public CompletableFuture<Void> enrichRating(Long movieId, String title) {
        String uri = UriComponentsBuilder.fromUriString("")
                .queryParam("t", title)
                .queryParam("apikey", omdbApiKey)
                .build()
                .toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(OmdbResponse.class)
                .onErrorResume(e -> {
                    logger.error("OMDb call failed for {}: {}", title, e.getMessage());
                    return Mono.empty();
                })
                .flatMap(response -> {
                    if (response.getImdbRating() != null && !response.getImdbRating().equals("N/A")) {
                        try {
                            double rating = Double.parseDouble(response.getImdbRating());
                            return Mono.fromRunnable(() -> movieService.updateRating(movieId, rating));
                        } catch (NumberFormatException e) {
                            logger.warn("Invalid IMDB rating for {}: {}", title, response.getImdbRating());
                        }
                    }
                    return Mono.empty();
                })
                .then()
                .toFuture();
    }
}
