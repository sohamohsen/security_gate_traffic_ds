package com.research.repository;

import com.research.model.GateLane;
import com.research.model.LaneStatus;

import java.util.List;
import java.util.Optional;

public class GateLaneRepository extends InMemoryRepository<GateLane> {
    public GateLaneRepository() {
        super("GateLane");
    }

    public Optional<GateLane> findByLaneNumber(int laneNumber) {
        return findOneBy(lane -> lane.getLaneNumber() == laneNumber);
    }

    public List<GateLane> findOpenLanes() {
        return findBy(lane -> lane.getStatus() == LaneStatus.OPEN);
    }
}