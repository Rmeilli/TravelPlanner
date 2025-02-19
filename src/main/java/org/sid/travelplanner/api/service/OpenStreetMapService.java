package org.sid.travelplanner.api.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.sid.travelplanner.api.client.OpenStreetMapClient;
import org.sid.travelplanner.api.dto.places.PlaceDTO;
import org.sid.travelplanner.api.dto.places.PlaceResponseDTO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenStreetMapService {
    public OpenStreetMapService(OpenStreetMapClient openStreetMapClient) {
        this.openStreetMapClient = openStreetMapClient;
    }

    private final OpenStreetMapClient openStreetMapClient;

    public List<PlaceDTO> searchPlaces(String query, String type) {
        PlaceResponseDTO[] response = openStreetMapClient.searchPlaces(query, type);
        return convertToPlaceDTOs(response);
    }

    public List<PlaceDTO> searchNearbyPlaces(double lat, double lon, String type, int radius) {
        PlaceResponseDTO[] response = openStreetMapClient.searchNearbyPlaces(lat, lon, type, radius);
        return convertToPlaceDTOs(response);
    }

    private List<PlaceDTO> convertToPlaceDTOs(PlaceResponseDTO[] responses) {
        return Arrays.stream(responses)
                .map(this::convertToPlaceDTO)
                .collect(Collectors.toList());
    }

    private PlaceDTO convertToPlaceDTO(PlaceResponseDTO response) {
        return PlaceDTO.builder()
                .id(response.getPlaceId())
                .name(response.getDisplayName())
                .address(response.getFormattedAddress())
                .latitude(response.getLat())
                .longitude(response.getLon())
                .type(response.getType())
                .category(response.getCategory())
                .build();
    }
}