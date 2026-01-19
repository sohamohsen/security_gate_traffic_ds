package com.research.model;// src/main/java/com/compoundgate/model/Resident.java

public class Resident extends BaseEntity {
    private String fullName;
    private String email;
    private String phone;
    private String unitNumber;

    public Resident(int id, String fullName, String email, String phone, String unitNumber) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.unitNumber = unitNumber;
    }

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getUnitNumber() { return unitNumber; }
    public void setUnitNumber(String unitNumber) { this.unitNumber = unitNumber; }

    @Override
    public String toString() {
        return String.format("Resident{id=%d, name='%s', unit='%s', email='%s', phone='%s'}",
                id, fullName, unitNumber, email, phone);
    }
}