package com.research.repository;

import com.research.model.GatePass;
import com.research.model.PassStatus;

import java.util.List;

public class GatePassRepository extends InMemoryRepository<GatePass> {
    public GatePassRepository() {
        super("GatePass");
    }

    public List<GatePass> findByVehicleId(int vehicleId) {
        return findBy(pass -> pass.getVehicle().getId() == vehicleId);
    }

    public List<GatePass> findByLaneId(int laneId) {
        return findBy(pass -> pass.getLane().getId() == laneId);
    }

    public List<GatePass> findPendingPasses() {
        return findBy(pass -> pass.getStatus() == PassStatus.PENDING);
    }
}