package org.sid.travelplanner.api.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.travelplanner.api.dto.places.PlaceResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class OpenStreetMapClient {
    public OpenStreetMapClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger logger = LoggerFactory.getLogger(OpenStreetMapClient.class);
    private final RestTemplate restTemplate;
    private static final String BASE_URL = "https://nominatim.openstreetmap.org";

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "TravelPlanner/1.0");
        return headers;
    }

    public PlaceResponseDTO[] searchPlaces(String query, String type) {
        // Correction ici : on enlève amenity et on utilise les bons paramètres
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/search")
                .queryParam("q", query + " " + type) // Combine la recherche avec le type
                .queryParam("format", "json")
                .queryParam("addressdetails", "1")
                .queryParam("limit", 10)
                .build()
                .toUriString();

        logger.info("Calling OpenStreetMap API with URL: {}", url);

        try {
            HttpEntity<?> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<PlaceResponseDTO[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    PlaceResponseDTO[].class
            );

            logger.info("Response status: {}", response.getStatusCode());
            return response.getBody();
        } catch (Exception e) {
            logger.error("Error calling OpenStreetMap API", e);
            throw new RuntimeException("Failed to fetch places from OpenStreetMap", e);
        }
    }

    public PlaceResponseDTO[] searchNearbyPlaces(double lat, double lon, String type, int radius) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/search")
                .queryParam("format", "json")
                .queryParam("addressdetails", "1")
                .queryParam("q", type) // Type comme terme de recherche
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("radius", radius)
                .build()
                .toUriString();

        logger.info("Calling OpenStreetMap API with URL: {}", url);

        try {
            HttpEntity<?> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<PlaceResponseDTO[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    PlaceResponseDTO[].class
            );

            logger.info("Response status: {}", response.getStatusCode());
            return response.getBody();
        } catch (Exception e) {
            logger.error("Error calling OpenStreetMap API", e);
            throw new RuntimeException("Failed to fetch nearby places from OpenStreetMap", e);
        }
    }
}