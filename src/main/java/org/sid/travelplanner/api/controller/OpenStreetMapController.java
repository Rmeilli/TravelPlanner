package org.sid.travelplanner.api.controller;

import lombok.RequiredArgsConstructor;
import org.sid.travelplanner.api.dto.places.PlaceDTO;
import org.sid.travelplanner.api.service.OpenStreetMapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/external/places")
public class OpenStreetMapController {
    public OpenStreetMapController(OpenStreetMapService openStreetMapService) {
        this.openStreetMapService = openStreetMapService;
    }

    private final OpenStreetMapService openStreetMapService;

    @GetMapping("/search")
    public ResponseEntity<List<PlaceDTO>> searchPlaces(
            @RequestParam String query,
            @RequestParam(required = false, defaultValue = "restaurant") String type) {
        return ResponseEntity.ok(openStreetMapService.searchPlaces(query, type));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<PlaceDTO>> searchNearbyPlaces(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(required = false, defaultValue = "restaurant") String type,
            @RequestParam(required = false, defaultValue = "1000") int radius) {
        return ResponseEntity.ok(openStreetMapService.searchNearbyPlaces(lat, lon, type, radius));
    }
}
