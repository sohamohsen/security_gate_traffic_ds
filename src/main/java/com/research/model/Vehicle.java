package com.research.model;// src/main/java/com/compoundgate/model/Vehicle.java


public class Vehicle extends BaseEntity {
    private String plateNumber;
    private Resident owner;
    private VehicleType vehicleType;
    private boolean isAllowed;

    public Vehicle(int id, String plateNumber, Resident owner, VehicleType vehicleType, boolean isAllowed) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.owner = owner;
        this.vehicleType = vehicleType;
        this.isAllowed = isAllowed;
    }

    // Getters and Setters
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public Resident getOwner() { return owner; }
    public void setOwner(Resident owner) { this.owner = owner; }
    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
    public boolean isAllowed() { return isAllowed; }
    public void setAllowed(boolean allowed) { isAllowed = allowed; }

    @Override
    public String toString() {
        return String.format("Vehicle{id=%d, plate='%s', owner='%s', type='%s', allowed=%s}",
                id, plateNumber, owner.getFullName(), vehicleType.getName(), isAllowed);
    }
}