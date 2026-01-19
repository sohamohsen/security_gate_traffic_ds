package com.research.model;

import com.research.model.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class VisitReservation extends BaseEntity {
    private String visitorName;
    private String vehiclePlate;
    private LocalDate visitDate;
    private LocalTime visitTime;
    private int numberOfPassengers;
    private boolean isCancelled;

    public VisitReservation(int id, String visitorName, String vehiclePlate,
                            LocalDate visitDate, LocalTime visitTime, int numberOfPassengers) {
        this.id = id;
        this.visitorName = visitorName;
        this.vehiclePlate = vehiclePlate;
        this.visitDate = visitDate;
        this.visitTime = visitTime;
        this.numberOfPassengers = numberOfPassengers;
        this.isCancelled = false;
    }

    // Getters and Setters
    public String getVisitorName() { return visitorName; }
    public void setVisitorName(String visitorName) { this.visitorName = visitorName; }
    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }
    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }
    public LocalTime getVisitTime() { return visitTime; }
    public void setVisitTime(LocalTime visitTime) { this.visitTime = visitTime; }
    public int getNumberOfPassengers() { return numberOfPassengers; }
    public void setNumberOfPassengers(int numberOfPassengers) { this.numberOfPassengers = numberOfPassengers; }
    public boolean isCancelled() { return isCancelled; }
    public void setCancelled(boolean cancelled) { isCancelled = cancelled; }

    public boolean isValidForAccess(LocalDateTime checkTime) {
        if (isCancelled) return false;

        LocalDateTime reservationDateTime = LocalDateTime.of(visitDate, visitTime);
        LocalDateTime startWindow = reservationDateTime.minusMinutes(30);
        LocalDateTime endWindow = reservationDateTime.plusMinutes(30);

        return !checkTime.isBefore(startWindow) && !checkTime.isAfter(endWindow);
    }

    @Override
    public String toString() {
        return String.format("VisitReservation{id=%d, visitor='%s', plate='%s', date=%s, time=%s, passengers=%d, cancelled=%s}",
                id, visitorName, vehiclePlate, visitDate, visitTime, numberOfPassengers, isCancelled);
    }
}