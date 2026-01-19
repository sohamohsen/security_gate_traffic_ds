package com.research.service;

import com.research.exception.BusinessRuleViolationException;
import com.research.exception.NotFoundException;
import com.research.model.Resident;
import com.research.model.Vehicle;
import com.research.model.VehicleType;
import com.research.repository.ResidentRepository;
import com.research.repository.VehicleRepository;
import com.research.repository.VehicleTypeRepository;

import java.util.List;
import java.util.Optional;

public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final ResidentRepository residentRepository;
    private final ValidationService validationService;

    public VehicleService(VehicleRepository vehicleRepository,
                          VehicleTypeRepository vehicleTypeRepository,
                          ResidentRepository residentRepository,
                          ValidationService validationService) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.residentRepository = residentRepository;
        this.validationService = validationService;
    }

    public Vehicle registerVehicle(int id, String plateNumber, int ownerId, int vehicleTypeId, boolean isAllowed) {
        validationService.validatePlateNumber(plateNumber);

        if (vehicleRepository.plateNumberExists(plateNumber)) {
            throw new BusinessRuleViolationException("Vehicle with plate number " + plateNumber + " already exists");
        }

        Resident owner = residentRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Resident", ownerId));

        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleTypeId)
                .orElseThrow(() -> new NotFoundException("VehicleType", vehicleTypeId));

        Vehicle vehicle = new Vehicle(id, plateNumber, owner, vehicleType, isAllowed);
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(int id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle", id));
    }

    public Vehicle updateVehicle(int id, String plateNumber, int ownerId, int vehicleTypeId, boolean isAllowed) {
        validationService.validatePlateNumber(plateNumber);

        Vehicle existingVehicle = getVehicleById(id);

        // Check if new plate number conflicts with other vehicles
        if (!existingVehicle.getPlateNumber().equalsIgnoreCase(plateNumber) &&
                vehicleRepository.plateNumberExists(plateNumber)) {
            throw new BusinessRuleViolationException("Vehicle with plate number " + plateNumber + " already exists");
        }

        Resident owner = residentRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Resident", ownerId));

        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleTypeId)
                .orElseThrow(() -> new NotFoundException("VehicleType", vehicleTypeId));

        existingVehicle.setPlateNumber(plateNumber);
        existingVehicle.setOwner(owner);
        existingVehicle.setVehicleType(vehicleType);
        existingVehicle.setAllowed(isAllowed);

        return vehicleRepository.update(existingVehicle);
    }

    public void deleteVehicle(int id) {
        vehicleRepository.delete(id);
    }

    public Optional<Vehicle> findVehicleByPlate(String plateNumber) {
        return vehicleRepository.findByPlateNumber(plateNumber);
    }

    public List<Vehicle> findVehiclesByOwner(int ownerId) {
        return vehicleRepository.findByOwnerId(ownerId);
    }

    public void toggleVehicleAccess(int vehicleId, boolean allowed) {
        Vehicle vehicle = getVehicleById(vehicleId);
        vehicle.setAllowed(allowed);
        vehicleRepository.update(vehicle);
    }
}