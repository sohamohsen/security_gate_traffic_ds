package com.research.model;


public class VehicleType extends BaseEntity {
    private String name;
    private String description;

    public VehicleType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format("VehicleType{id=%d, name='%s', description='%s'}",
                id, name, description);
    }
}