package org.sid.travelplanner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.travelplanner.dto.TripDTO;
import org.sid.travelplanner.exception.ResourceNotFoundException;
import org.sid.travelplanner.model.Trip;
import org.sid.travelplanner.model.TripParticipant;
import org.sid.travelplanner.model.TripRole;
import org.sid.travelplanner.model.User;
import org.sid.travelplanner.repository.TripParticipantRepository;
import org.sid.travelplanner.repository.TripRepository;
import org.sid.travelplanner.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Slf4j
@Service
//@RequiredArgsConstructor
@Transactional
public class TripService {
    public TripService(TripRepository tripRepository, UserRepository userRepository, TripParticipantRepository tripParticipantRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.tripParticipantRepository = tripParticipantRepository;
    }


    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private static final Logger logger = Logger.getLogger(TripService.class.getName());
    private final TripParticipantRepository tripParticipantRepository;



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
    public void addParticipant(Long tripId, String userEmail) {
        Trip trip = getTripById(tripId);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email : " + userEmail));

        // Vérifie si l'utilisateur est déjà participant
        if (tripParticipantRepository.existsByTripAndUser(trip, user)) {
            throw new RuntimeException("L'utilisateur est déjà participant à ce voyage");
        }

        // Crée un nouveau participant
        TripParticipant participant = new TripParticipant();
        participant.setTrip(trip);
        participant.setUser(user);
        participant.setRole(TripRole.PARTICIPANT); // Le rôle par défaut
        participant.setJoinedAt(LocalDateTime.now());

        tripParticipantRepository.save(participant);
    }
}