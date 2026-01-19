package com.research.service;

import com.research.exception.BusinessRuleViolationException;
import com.research.exception.NotFoundException;
import com.research.model.*;
import com.research.repository.GatePassRepository;
import com.research.repository.VehicleRepository;
import com.research.repository.VisitReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TrafficService {
    private final GatePassRepository passRepository;
    private final VehicleRepository vehicleRepository;
    private final GateLaneService laneService;
    private final VisitReservationRepository reservationRepository;
    private final ValidationService validationService;

    public TrafficService(GatePassRepository passRepository,
                          VehicleRepository vehicleRepository,
                          GateLaneService laneService,
                          VisitReservationRepository reservationRepository,
                          ValidationService validationService) {
        this.passRepository = passRepository;
        this.vehicleRepository = vehicleRepository;
        this.laneService = laneService;
        this.reservationRepository = reservationRepository;
        this.validationService = validationService;
    }

    public GatePass requestEntry(int vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new NotFoundException("Vehicle", vehicleId));

        if (!vehicle.isAllowed()) {
            throw new BusinessRuleViolationException("Vehicle " + vehicle.getPlateNumber() + " is not allowed to enter");
        }

        GateLane lane = laneService.getAvailableLane()
                .orElseThrow(() -> new BusinessRuleViolationException("No available lanes for entry"));

        GatePass gatePass = new GatePass(0, vehicle, lane, PassDirection.ENTRY);
        return passRepository.save(gatePass);
    }

    public GatePass requestExit(int vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new NotFoundException("Vehicle", vehicleId));

        GateLane lane = laneService.getAvailableLane()
                .orElseThrow(() -> new BusinessRuleViolationException("No available lanes for exit"));

        GatePass gatePass = new GatePass(0, vehicle, lane, PassDirection.EXIT);
        return passRepository.save(gatePass);
    }

    public GatePass requestVisitorEntry(String plateNumber) {
        validationService.validatePlateNumber(plateNumber);

        // Check for valid reservation
        VisitReservation reservation = reservationRepository.findByVehiclePlate(plateNumber)
                .orElseThrow(() -> new NotFoundException("Visit reservation for plate", plateNumber));

        if (!reservation.isValidForAccess(LocalDateTime.now())) {
            throw new BusinessRuleViolationException("Visitor access not allowed at this time");
        }

        GateLane lane = laneService.getAvailableLane()
                .orElseThrow(() -> new BusinessRuleViolationException("No available lanes for visitor entry"));

        // Create temporary vehicle for visitor
        Vehicle tempVehicle = new Vehicle(0, plateNumber, null, null, true);
        GatePass gatePass = new GatePass(0, tempVehicle, lane, PassDirection.ENTRY);
        gatePass.setStatus(PassStatus.APPROVED);

        return passRepository.save(gatePass);
    }

    public GatePass approvePass(int passId) {
        GatePass pass = passRepository.findById(passId)
                .orElseThrow(() -> new NotFoundException("GatePass", passId));

        if (pass.getStatus() != PassStatus.PENDING) {
            throw new BusinessRuleViolationException("Cannot approve non-pending pass");
        }

        if (pass.getLane().getStatus() != LaneStatus.OPEN) {
            throw new BusinessRuleViolationException("Lane is not available");
        }

        pass.setStatus(PassStatus.APPROVED);
        laneService.increaseLaneLoad(pass.getLane().getId());

        return passRepository.update(pass);
    }

    public GatePass denyPass(int passId) {
        GatePass pass = passRepository.findById(passId)
                .orElseThrow(() -> new NotFoundException("GatePass", passId));

        if (pass.getStatus() != PassStatus.PENDING) {
            throw new BusinessRuleViolationException("Cannot deny non-pending pass");
        }

        pass.setStatus(PassStatus.DENIED);
        return passRepository.update(pass);
    }

    public GatePass completePass(int passId) {
        GatePass pass = passRepository.findById(passId)
                .orElseThrow(() -> new NotFoundException("GatePass", passId));

        if (pass.getStatus() != PassStatus.APPROVED) {
            throw new BusinessRuleViolationException("Cannot complete non-approved pass");
        }

        pass.setStatus(PassStatus.COMPLETED);
        laneService.decreaseLaneLoad(pass.getLane().getId());

        return passRepository.update(pass);
    }

    public List<GatePass> getAllPasses() {
        return passRepository.findAll();
    }

    public List<GatePass> getPendingPasses() {
        return passRepository.findPendingPasses();
    }

    public List<GatePass> getPassesByLane(int laneId) {
        return passRepository.findByLaneId(laneId);
    }

    public List<GatePass> getPassesByVehicle(int vehicleId) {
        return passRepository.findByVehicleId(vehicleId);
    }

    public int getLaneTrafficCount(int laneId, LocalDateTime start, LocalDateTime end) {
        return (int) passRepository.findAll().stream()
                .filter(pass -> pass.getLane().getId() == laneId)
                .filter(pass -> !pass.getPassTime().isBefore(start) && !pass.getPassTime().isAfter(end))
                .count();
    }
}