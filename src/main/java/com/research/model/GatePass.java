package com.research.model;

import java.time.LocalDateTime;

public class GatePass extends BaseEntity {
    private Vehicle vehicle;
    private GateLane lane;
    private PassDirection direction;
    private PassStatus status;
    private LocalDateTime passTime;

    public GatePass(int id, Vehicle vehicle, GateLane lane, PassDirection direction) {
        this.id = id;
        this.vehicle = vehicle;
        this.lane = lane;
        this.direction = direction;
        this.status = PassStatus.PENDING;
        this.passTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public GateLane getLane() { return lane; }
    public void setLane(GateLane lane) { this.lane = lane; }
    public PassDirection getDirection() { return direction; }
    public void setDirection(PassDirection direction) { this.direction = direction; }
    public PassStatus getStatus() { return status; }
    public void setStatus(PassStatus status) { this.status = status; }
    public LocalDateTime getPassTime() { return passTime; }
    public void setPassTime(LocalDateTime passTime) { this.passTime = passTime; }

    @Override
    public String toString() {
        return String.format("GatePass{id=%d, vehicle='%s', lane=%d, direction=%s, status=%s, time=%s}",
                id, vehicle.getPlateNumber(), lane.getLaneNumber(), direction, status, passTime);
    }
}