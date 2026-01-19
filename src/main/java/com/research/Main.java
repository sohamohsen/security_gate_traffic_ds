package com.research;

import com.research.model.VehicleType;
import com.research.repository.*;
import com.research.service.*;
import com.research.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize repositories
            ResidentRepository residentRepository = new ResidentRepository();
            VehicleTypeRepository vehicleTypeRepository = new VehicleTypeRepository();
            VehicleRepository vehicleRepository = new VehicleRepository();
            GateLaneRepository laneRepository = new GateLaneRepository();
            GatePassRepository passRepository = new GatePassRepository();
            VisitReservationRepository reservationRepository = new VisitReservationRepository();

            // Initialize validation service
            ValidationService validationService = new ValidationService();

            // Initialize vehicle types
            initializeVehicleTypes(vehicleTypeRepository);

            // Initialize services
            ResidentService residentService = new ResidentService(residentRepository, validationService);
            VehicleService vehicleService = new VehicleService(
                    vehicleRepository, vehicleTypeRepository, residentRepository, validationService);
            GateLaneService laneService = new GateLaneService(laneRepository, validationService);
            VisitorService visitorService = new VisitorService(reservationRepository, validationService);
            TrafficService trafficService = new TrafficService(
                    passRepository, vehicleRepository, laneService, reservationRepository, validationService);

            // Initialize and start UI
            ConsoleUI ui = new ConsoleUI(
                    residentService, vehicleService, laneService,
                    trafficService, visitorService, validationService);

            ui.start();

        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void initializeVehicleTypes(VehicleTypeRepository repository) {
        repository.save(new VehicleType(1, "Car", "Passenger car"));
        repository.save(new VehicleType(2, "SUV", "Sports Utility Vehicle"));
        repository.save(new VehicleType(3, "Motorcycle", "Two-wheeler"));
        repository.save(new VehicleType(4, "Truck", "Commercial vehicle"));
        repository.save(new VehicleType(5, "Van", "Passenger van"));
    }
}