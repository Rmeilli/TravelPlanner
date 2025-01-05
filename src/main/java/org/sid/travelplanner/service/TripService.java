package org.sid.travelplanner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.travelplanner.dto.TripDTO;
import org.sid.travelplanner.model.Trip;
import org.sid.travelplanner.model.User;
import org.sid.travelplanner.repository.TripRepository;
import org.sid.travelplanner.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

@Slf4j
@Service
//@RequiredArgsConstructor
@Transactional
public class TripService {
    public TripService(TripRepository tripRepository, UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private static final Logger logger = Logger.getLogger(TripService.class.getName());


    // Créer un nouveau voyage
    public Trip createTrip(TripDTO tripDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Trip trip = new Trip();
        trip.setDestination(tripDTO.getDestination());
        trip.setStartDate(tripDTO.getStartDate());
        trip.setEndDate(tripDTO.getEndDate());
        trip.setDescription(tripDTO.getDescription());
        trip.setBudget(tripDTO.getBudget());
        trip.setCreator(user);

        return tripRepository.save(trip);
    }

    // Obtenir tous les voyages d'un utilisateur
//    public List<Trip> getUserTrips(Long userId) {
//        return tripRepository.findByCreatorId(userId);
//    }
    public List<Trip> getUserTrips(Long userId) {
        logger.info("Recherche des voyages pour l'utilisateur ID: " + userId);
        try {
            List<Trip> trips = tripRepository.findByCreatorId(userId);
            logger.info("Nombre de voyages trouvés: " + trips.size());
            return trips;
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération des voyages: " + e.getMessage());
            throw e;
        }
    }


    // Obtenir un voyage par son ID
    public Trip getTripById(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Voyage non trouvé"));
    }

    // Mettre à jour un voyage
    public Trip updateTrip(Long tripId, TripDTO tripDTO) {
        Trip trip = getTripById(tripId);

        trip.setDestination(tripDTO.getDestination());
        trip.setStartDate(tripDTO.getStartDate());
        trip.setEndDate(tripDTO.getEndDate());
        trip.setDescription(tripDTO.getDescription());
        trip.setBudget(tripDTO.getBudget());

        return tripRepository.save(trip);
    }

    // Supprimer un voyage
    public void deleteTrip(Long tripId) {
        tripRepository.deleteById(tripId);
    }
}