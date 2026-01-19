package com.research.service;

import com.research.exception.ValidationException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Pattern;

public class ValidationService {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9]{10,15}$");
    private static final Pattern PLATE_PATTERN =
            Pattern.compile("^[A-Z0-9-]{3,15}$");

    public void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }
    }

    public void validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new ValidationException("Phone cannot be empty");
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("Invalid phone number format");
        }
    }

    public void validatePlateNumber(String plateNumber) {
        if (plateNumber == null || plateNumber.isBlank()) {
            throw new ValidationException("Plate number cannot be empty");
        }
        if (!PLATE_PATTERN.matcher(plateNumber).matches()) {
            throw new ValidationException("Invalid plate number format");
        }
    }

    public void validateNotNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new ValidationException(fieldName + " cannot be null");
        }
    }

    public void validateNotEmpty(String str, String fieldName) {
        if (str == null || str.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty");
        }
    }

    public void validatePositive(int number, String fieldName) {
        if (number <= 0) {
            throw new ValidationException(fieldName + " must be positive");
        }
    }

    public void validateFutureDate(LocalDate date, String fieldName) {
        if (date.isBefore(LocalDate.now())) {
            throw new ValidationException(fieldName + " must be in the future");
        }
    }

    public void validateTimeWindow(LocalTime time) {
        LocalTime minTime = LocalTime.of(6, 0);
        LocalTime maxTime = LocalTime.of(22, 0);

        if (time.isBefore(minTime) || time.isAfter(maxTime)) {
            throw new ValidationException("Visit time must be between 06:00 and 22:00");
        }
    }

    public void validatePassengers(int passengers) {
        if (passengers < 1) {
            throw new ValidationException("Number of passengers must be at least 1");
        }
        if (passengers > 10) {
            throw new ValidationException("Number of passengers cannot exceed 10");
        }
    }
}