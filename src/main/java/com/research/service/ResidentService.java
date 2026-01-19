package com.research.service;

import com.research.exception.NotFoundException;
import com.research.model.Resident;
import com.research.repository.ResidentRepository;

import java.util.List;
import java.util.Optional;

public class ResidentService {
    private final ResidentRepository residentRepository;
    private final ValidationService validationService;

    public ResidentService(ResidentRepository residentRepository, ValidationService validationService) {
        this.residentRepository = residentRepository;
        this.validationService = validationService;
    }

    public Resident addResident(int id, String fullName, String email, String phone, String unitNumber) {
        validationService.validateNotEmpty(fullName, "Full name");
        validationService.validateEmail(email);
        validationService.validatePhone(phone);
        validationService.validateNotEmpty(unitNumber, "Unit number");

        Resident resident = new Resident(id, fullName, email, phone, unitNumber);
        return residentRepository.save(resident);
    }

    public List<Resident> getAllResidents() {
        return residentRepository.findAll();
    }

    public Resident getResidentById(int id) {
        return residentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Resident", id));
    }

    public Resident updateResident(int id, String fullName, String email, String phone, String unitNumber) {
        validationService.validateNotEmpty(fullName, "Full name");
        validationService.validateEmail(email);
        validationService.validatePhone(phone);
        validationService.validateNotEmpty(unitNumber, "Unit number");

        Resident resident = getResidentById(id);
        resident.setFullName(fullName);
        resident.setEmail(email);
        resident.setPhone(phone);
        resident.setUnitNumber(unitNumber);

        return residentRepository.update(resident);
    }

    public void deleteResident(int id) {
        residentRepository.delete(id);
    }

    public List<Resident> searchResidents(String searchTerm) {
        return residentRepository.findByNameContains(searchTerm);
    }

    public Optional<Resident> findByUnitNumber(String unitNumber) {
        return residentRepository.findByUnitNumber(unitNumber);
    }
}