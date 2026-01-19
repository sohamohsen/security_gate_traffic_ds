package com.research.repository;

import com.research.model.VehicleType;
import com.research.repository.InMemoryRepository;

import java.util.Optional;

public class VehicleTypeRepository extends InMemoryRepository<VehicleType> {
    public VehicleTypeRepository() {
        super("VehicleType");
    }

    public Optional<VehicleType> findByName(String name) {
        return findOneBy(type -> type.getName().equalsIgnoreCase(name));
    }
}
