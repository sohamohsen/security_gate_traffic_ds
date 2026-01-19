package com.research.repository;

import com.research.model.Vehicle;

import java.util.List;
import java.util.Optional;

public class VehicleRepository extends InMemoryRepository<Vehicle> {
    public VehicleRepository() {
        super("Vehicle");
    }

    public Optional<Vehicle> findByPlateNumber(String plateNumber) {
        return findOneBy(vehicle -> vehicle.getPlateNumber().equalsIgnoreCase(plateNumber));
    }

    public List<Vehicle> findByOwnerId(int ownerId) {
        return findBy(vehicle -> vehicle.getOwner().getId() == ownerId);
    }

    public boolean plateNumberExists(String plateNumber) {
        return findByPlateNumber(plateNumber).isPresent();
    }
}