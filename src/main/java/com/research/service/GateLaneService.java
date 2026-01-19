package com.research.service;

import com.research.exception.BusinessRuleViolationException;
import com.research.exception.NotFoundException;
import com.research.model.GateLane;
import com.research.model.LaneStatus;
import com.research.repository.GateLaneRepository;

import java.util.List;
import java.util.Optional;

public class GateLaneService {
    private final GateLaneRepository laneRepository;
    private final ValidationService validationService;

    public GateLaneService(GateLaneRepository laneRepository, ValidationService validationService) {
        this.laneRepository = laneRepository;
        this.validationService = validationService;
    }

    public GateLane addLane(int id, int laneNumber, int capacityPerMinute) {
        validationService.validatePositive(laneNumber, "Lane number");
        validationService.validatePositive(capacityPerMinute, "Capacity per minute");

        if (laneRepository.findByLaneNumber(laneNumber).isPresent()) {
            throw new BusinessRuleViolationException("Lane with number " + laneNumber + " already exists");
        }

        GateLane lane = new GateLane(id, laneNumber, capacityPerMinute, LaneStatus.OPEN);
        return laneRepository.save(lane);
    }

    public List<GateLane> getAllLanes() {
        return laneRepository.findAll();
    }

    public GateLane getLaneById(int id) {
        return laneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("GateLane", id));
    }

    public GateLane updateLane(int id, int laneNumber, int capacityPerMinute) {
        validationService.validatePositive(laneNumber, "Lane number");
        validationService.validatePositive(capacityPerMinute, "Capacity per minute");

        GateLane lane = getLaneById(id);

        // Check if new lane number conflicts with other lanes
        Optional<GateLane> existingWithNumber = laneRepository.findByLaneNumber(laneNumber);
        if (existingWithNumber.isPresent() && existingWithNumber.get().getId() != id) {
            throw new BusinessRuleViolationException("Lane with number " + laneNumber + " already exists");
        }

        lane.setLaneNumber(laneNumber);
        lane.setCapacityPerMinute(capacityPerMinute);

        return laneRepository.update(lane);
    }

    public GateLane updateLaneStatus(int laneId, LaneStatus status) {
        GateLane lane = getLaneById(laneId);

        if (lane.getStatus() == LaneStatus.BUSY && status != LaneStatus.BUSY) {
            throw new BusinessRuleViolationException("Cannot change status from BUSY directly");
        }

        lane.setStatus(status);
        return laneRepository.update(lane);
    }

    public List<GateLane> getAvailableLanes() {
        return laneRepository.findOpenLanes().stream()
                .filter(GateLane::hasCapacity)
                .toList();
    }

    public Optional<GateLane> getAvailableLane() {
        return laneRepository.findOpenLanes().stream()
                .filter(GateLane::hasCapacity)
                .findFirst();
    }

    public void increaseLaneLoad(int laneId) {
        GateLane lane = getLaneById(laneId);
        lane.increaseLoad();

        if (!lane.hasCapacity()) {
            lane.setStatus(LaneStatus.BUSY);
        }
        laneRepository.update(lane);
    }

    public void decreaseLaneLoad(int laneId) {
        GateLane lane = getLaneById(laneId);
        lane.decreaseLoad();

        if (lane.hasCapacity() && lane.getStatus() == LaneStatus.BUSY) {
            lane.setStatus(LaneStatus.OPEN);
        }
        laneRepository.update(lane);
    }

    public double getLaneUtilization(int laneId) {
        GateLane lane = getLaneById(laneId);
        return (double) lane.getCurrentLoad() / lane.getCapacityPerMinute() * 100;
    }
}