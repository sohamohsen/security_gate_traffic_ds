package com.research.model;

public class GateStaff extends BaseEntity {
    private String fullName;
    private StaffRole role;
    private String email;
    private String phone;

    public GateStaff(int id, String fullName, StaffRole role, String email, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.role = role;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public StaffRole getRole() { return role; }
    public void setRole(StaffRole role) { this.role = role; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return String.format("GateStaff{id=%d, name='%s', role=%s, email='%s', phone='%s'}",
                id, fullName, role, email, phone);
    }
}