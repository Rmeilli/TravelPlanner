package org.sid.travelplanner.controller;

import lombok.RequiredArgsConstructor;
import org.sid.travelplanner.dto.TripDTO;
import org.sid.travelplanner.model.Trip;
import org.sid.travelplanner.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
//@RequiredArgsConstructor
public class TripController {
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody TripDTO tripDTO, @RequestParam Long userId) {
        Trip newTrip = tripService.createTrip(tripDTO, userId);
        return ResponseEntity.ok(newTrip);
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Trip>> getUserTrips(@PathVariable Long userId) {
        try {
            List<Trip> trips = tripService.getUserTrips(userId);
            return ResponseEntity.ok(trips);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des voyages: " + e.getMessage());
        }
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTrip(@PathVariable Long tripId) {
        Trip trip = tripService.getTripById(tripId);
        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long tripId, @RequestBody TripDTO tripDTO) {
        Trip updatedTrip = tripService.updateTrip(tripId, tripDTO);
        return ResponseEntity.ok(updatedTrip);
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
        return ResponseEntity.ok().build();
    }
}