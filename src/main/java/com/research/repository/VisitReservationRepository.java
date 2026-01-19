package com.research.repository;

import com.research.model.VisitReservation;

import java.util.List;
import java.util.Optional;

public class VisitReservationRepository extends InMemoryRepository<VisitReservation> {
    public VisitReservationRepository() {
        super("VisitReservation");
    }

    public Optional<VisitReservation> findByVehiclePlate(String plateNumber) {
        return findOneBy(reservation -> reservation.getVehiclePlate().equalsIgnoreCase(plateNumber)
                && !reservation.isCancelled());
    }

    public List<VisitReservation> findByVisitorName(String visitorName) {
        return findBy(reservation -> reservation.getVisitorName().equalsIgnoreCase(visitorName));
    }

    public List<VisitReservation> findActiveReservations() {
        return findBy(reservation -> !reservation.isCancelled());
    }
}