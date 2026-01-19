package com.research.model;


public class GateLane extends BaseEntity {
    private int laneNumber;
    private int capacityPerMinute;
    private LaneStatus status;
    private int currentLoad;

    public GateLane(int id, int laneNumber, int capacityPerMinute, LaneStatus status) {
        this.id = id;
        this.laneNumber = laneNumber;
        this.capacityPerMinute = capacityPerMinute;
        this.status = status;
        this.currentLoad = 0;
    }

    // Getters and Setters
    public int getLaneNumber() { return laneNumber; }
    public void setLaneNumber(int laneNumber) { this.laneNumber = laneNumber; }
    public int getCapacityPerMinute() { return capacityPerMinute; }
    public void setCapacityPerMinute(int capacityPerMinute) { this.capacityPerMinute = capacityPerMinute; }
    public LaneStatus getStatus() { return status; }
    public void setStatus(LaneStatus status) { this.status = status; }
    public int getCurrentLoad() { return currentLoad; }
    public void setCurrentLoad(int currentLoad) { this.currentLoad = currentLoad; }

    public boolean hasCapacity() {
        return currentLoad < capacityPerMinute;
    }

    public void increaseLoad() {
        if (currentLoad < capacityPerMinute) {
            currentLoad++;
        }
    }

    public void decreaseLoad() {
        if (currentLoad > 0) {
            currentLoad--;
        }
    }

    @Override
    public String toString() {
        return String.format("GateLane{id=%d, laneNumber=%d, capacity=%d, status=%s, currentLoad=%d}",
                id, laneNumber, capacityPerMinute, status, currentLoad);
    }
}