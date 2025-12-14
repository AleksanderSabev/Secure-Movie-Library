package org.example.movielibraryapi.services;

import org.example.movielibraryapi.models.dtos.OmdbResponse;
import org.example.movielibraryapi.services.contracts.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieRatingEnrichmentServiceImplTest {

    @Mock
    private MovieService movieService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private MovieRatingEnrichmentServiceImpl service;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        service = new MovieRatingEnrichmentServiceImpl(movieService, webClientBuilder, "fake-api-key");
    }

    @Test
    void enrichRating_should_updateRating_when_validImdbRating() {
        OmdbResponse response = new OmdbResponse();
        response.setImdbRating("8.7");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OmdbResponse.class)).thenReturn(Mono.just(response));

        CompletableFuture<Void> future = service.enrichRating(1L, "Inception");
        future.join();

        verify(movieService).updateRating(1L, 8.7);
        verifyNoMoreInteractions(movieService);
    }

    @Test
    void enrichRating_should_notUpdate_when_imdbRatingIsNA() {
        OmdbResponse response = new OmdbResponse();
        response.setImdbRating("N/A");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OmdbResponse.class)).thenReturn(Mono.just(response));

        CompletableFuture<Void> future = service.enrichRating(1L, "Inception");
        future.join();

        verify(movieService, never()).updateRating(anyLong(), anyDouble());
    }

    @Test
    void enrichRating_should_notUpdate_when_imdbRatingIsInvalid() {
        OmdbResponse response = new OmdbResponse();
        response.setImdbRating("invalid");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OmdbResponse.class)).thenReturn(Mono.just(response));

        CompletableFuture<Void> future = service.enrichRating(1L, "Inception");
        future.join();

        verify(movieService, never()).updateRating(anyLong(), anyDouble());
    }

    @Test
    void enrichRating_should_completeNormally_when_webClientThrows() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OmdbResponse.class)).thenReturn(Mono.error(new RuntimeException("OMDb error")));

        CompletableFuture<Void> future = service.enrichRating(1L, "Inception");
        assertDoesNotThrow(future::join); // now it truly completes normally
        verify(movieService, never()).updateRating(anyLong(), anyDouble());
    }
}
