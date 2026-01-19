package com.research.service;

import com.research.exception.BusinessRuleViolationException;
import com.research.exception.NotFoundException;
import com.research.model.VisitReservation;
import com.research.repository.VisitReservationRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class VisitorService {
    private final VisitReservationRepository reservationRepository;
    private final ValidationService validationService;

    public VisitorService(VisitReservationRepository reservationRepository, ValidationService validationService) {
        this.reservationRepository = reservationRepository;
        this.validationService = validationService;
    }

    public VisitReservation createReservation(int id, String visitorName, String vehiclePlate,
                                              LocalDate visitDate, LocalTime visitTime, int numberOfPassengers) {
        validationService.validateNotEmpty(visitorName, "Visitor name");
        validationService.validatePlateNumber(vehiclePlate);
        validationService.validateFutureDate(visitDate, "Visit date");
        validationService.validateTimeWindow(visitTime);
        validationService.validatePassengers(numberOfPassengers);

        // Check for existing reservation for same vehicle on same date
        List<VisitReservation> existingReservations = reservationRepository.findAll().stream()
                .filter(r -> r.getVehiclePlate().equalsIgnoreCase(vehiclePlate))
                .filter(r -> r.getVisitDate().equals(visitDate))
                .filter(r -> !r.isCancelled())
                .toList();

        if (!existingReservations.isEmpty()) {
            throw new BusinessRuleViolationException("Vehicle already has a reservation for this date");
        }

        VisitReservation reservation = new VisitReservation(id, visitorName, vehiclePlate,
                visitDate, visitTime, numberOfPassengers);
        return reservationRepository.save(reservation);
    }

    public List<VisitReservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public VisitReservation getReservationById(int id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("VisitReservation", id));
    }

    public void cancelReservation(int id) {
        VisitReservation reservation = getReservationById(id);
        reservation.setCancelled(true);
        reservationRepository.update(reservation);
    }

    public boolean validateVisitorAccess(String vehiclePlate) {
        return reservationRepository.findByVehiclePlate(vehiclePlate)
                .map(reservation -> reservation.isValidForAccess(java.time.LocalDateTime.now()))
                .orElse(false);
    }

    public List<VisitReservation> getActiveReservations() {
        return reservationRepository.findActiveReservations();
    }

    public List<VisitReservation> getReservationsByVisitor(String visitorName) {
        return reservationRepository.findByVisitorName(visitorName);
    }
}