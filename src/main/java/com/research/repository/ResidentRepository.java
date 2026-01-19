package com.research.repository;

import com.research.model.Resident;
import java.util.List;
import java.util.Optional;

public class ResidentRepository extends InMemoryRepository<Resident> {
    public ResidentRepository() {
        super("Resident");
    }

    public Optional<Resident> findByUnitNumber(String unitNumber) {
        return findOneBy(resident -> resident.getUnitNumber().equalsIgnoreCase(unitNumber));
    }

    public List<Resident> findByNameContains(String name) {
        return findBy(resident -> resident.getFullName().toLowerCase().contains(name.toLowerCase()));
    }
}