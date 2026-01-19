package com.research.repository;

import com.research.model.GateStaff;

public class GateStaffRepository extends InMemoryRepository<GateStaff> {
    public GateStaffRepository() {
        super("GateStaff");
    }
}