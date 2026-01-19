package com.research.ui;

import com.research.exception.CompoundGateException;
import com.research.model.*;
import com.research.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private final ResidentService residentService;
    private final VehicleService vehicleService;
    private final GateLaneService laneService;
    private final TrafficService trafficService;
    private final VisitorService visitorService;
    private final ValidationService validationService;

    private boolean running = true;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public ConsoleUI(ResidentService residentService, VehicleService vehicleService,
                     GateLaneService laneService, TrafficService trafficService,
                     VisitorService visitorService, ValidationService validationService) {
        this.scanner = new Scanner(System.in);
        this.residentService = residentService;
        this.vehicleService = vehicleService;
        this.laneService = laneService;
        this.trafficService = trafficService;
        this.visitorService = visitorService;
        this.validationService = validationService;
        initializeSampleData();
    }

    private void initializeSampleData() {
        try {
            // Create sample vehicle types
            VehicleType carType = new VehicleType(1, "Car", "Passenger car");
            VehicleType suvType = new VehicleType(2, "SUV", "Sports Utility Vehicle");
            VehicleType motorcycleType = new VehicleType(3, "Motorcycle", "Two-wheeler");

            // Create sample residents
            Resident resident1 = residentService.addResident(1, "John Doe", "john@example.com", "+1234567890", "A101");
            Resident resident2 = residentService.addResident(2, "Jane Smith", "jane@example.com", "+0987654321", "B202");

            // Create sample vehicles
            vehicleService.registerVehicle(1, "ABC-123", 1, 1, true);
            vehicleService.registerVehicle(2, "XYZ-789", 2, 2, true);

            // Create sample lanes
            laneService.addLane(1, 1, 10);
            laneService.addLane(2, 2, 15);

            // Create sample reservations
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            visitorService.createReservation(1, "Visitor One", "VIS-001", tomorrow, LocalTime.of(14, 0), 2);

        } catch (Exception e) {
            System.out.println("Warning: Could not initialize sample data: " + e.getMessage());
        }
    }

    public void start() {
        System.out.println("=== Compound Gate Traffic Management System ===");
        System.out.println("Welcome, Administrator!");

        while (running) {
            showMainMenu();
        }

        scanner.close();
        System.out.println("System shutdown. Goodbye!");
    }

    private void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Resident Management");
        System.out.println("2. Vehicle Management");
        System.out.println("3. Gate Lane Management");
        System.out.println("4. Traffic Management");
        System.out.println("5. Visitor Management");
        System.out.println("6. Driver Operations");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> showResidentMenu();
                case 2 -> showVehicleMenu();
                case 3 -> showLaneMenu();
                case 4 -> showTrafficMenu();
                case 5 -> showVisitorMenu();
                case 6 -> showDriverMenu();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Clear invalid input
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void showResidentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== RESIDENT MANAGEMENT ===");
            System.out.println("1. Add Resident");
            System.out.println("2. View All Residents");
            System.out.println("3. Update Resident");
            System.out.println("4. Delete Resident");
            System.out.println("5. Search Resident");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> addResident();
                    case 2 -> viewAllResidents();
                    case 3 -> updateResident();
                    case 4 -> deleteResident();
                    case 5 -> searchResident();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private void addResident() {
        System.out.println("\n--- Add New Resident ---");

        try {
            System.out.print("Enter resident ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter full name: ");
            String name = scanner.nextLine();

            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            System.out.print("Enter phone: ");
            String phone = scanner.nextLine();

            System.out.print("Enter unit number: ");
            String unit = scanner.nextLine();

            Resident resident = residentService.addResident(id, name, email, phone, unit);
            System.out.println("Resident added successfully: " + resident);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void viewAllResidents() {
        System.out.println("\n--- All Residents ---");
        List<Resident> residents = residentService.getAllResidents();

        if (residents.isEmpty()) {
            System.out.println("No residents found.");
        } else {
            for (Resident resident : residents) {
                System.out.println(resident);
            }
            System.out.println("Total: " + residents.size() + " residents");
        }
    }

    private void updateResident() {
        System.out.println("\n--- Update Resident ---");

        try {
            System.out.print("Enter resident ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter new full name: ");
            String name = scanner.nextLine();

            System.out.print("Enter new email: ");
            String email = scanner.nextLine();

            System.out.print("Enter new phone: ");
            String phone = scanner.nextLine();

            System.out.print("Enter new unit number: ");
            String unit = scanner.nextLine();

            Resident updated = residentService.updateResident(id, name, email, phone, unit);
            System.out.println("Resident updated successfully: " + updated);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void deleteResident() {
        System.out.println("\n--- Delete Resident ---");

        try {
            System.out.print("Enter resident ID to delete: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Are you sure? (yes/no): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("yes")) {
                residentService.deleteResident(id);
                System.out.println("Resident deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void searchResident() {
        System.out.println("\n--- Search Resident ---");

        try {
            System.out.print("Enter search term (name): ");
            String searchTerm = scanner.nextLine();

            List<Resident> results = residentService.searchResidents(searchTerm);

            if (results.isEmpty()) {
                System.out.println("No residents found matching: " + searchTerm);
            } else {
                System.out.println("Search results:");
                for (Resident resident : results) {
                    System.out.println(resident);
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void showVehicleMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== VEHICLE MANAGEMENT ===");
            System.out.println("1. Register Vehicle");
            System.out.println("2. View All Vehicles");
            System.out.println("3. Update Vehicle");
            System.out.println("4. Delete Vehicle");
            System.out.println("5. Search by Plate Number");
            System.out.println("6. Toggle Vehicle Access");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> registerVehicle();
                    case 2 -> viewAllVehicles();
                    case 3 -> updateVehicle();
                    case 4 -> deleteVehicle();
                    case 5 -> searchVehicleByPlate();
                    case 6 -> toggleVehicleAccess();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private void registerVehicle() {
        System.out.println("\n--- Register Vehicle ---");

        try {
            System.out.print("Enter vehicle ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter plate number: ");
            String plate = scanner.nextLine();

            System.out.print("Enter owner resident ID: ");
            int ownerId = scanner.nextInt();

            System.out.println("Available vehicle types:");
            System.out.println("1. Car");
            System.out.println("2. SUV");
            System.out.println("3. Motorcycle");
            System.out.print("Select vehicle type (1-3): ");
            int typeChoice = scanner.nextInt();

            System.out.print("Is vehicle allowed? (true/false): ");
            boolean allowed = scanner.nextBoolean();
            scanner.nextLine();

            vehicleService.registerVehicle(id, plate, ownerId, typeChoice, allowed);
            System.out.println("Vehicle registered successfully.");
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void viewAllVehicles() {
        System.out.println("\n--- All Vehicles ---");
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
        } else {
            for (Vehicle vehicle : vehicles) {
                System.out.println(vehicle);
            }
        }
    }

    private void showLaneMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== GATE LANE MANAGEMENT ===");
            System.out.println("1. Add Lane");
            System.out.println("2. View All Lanes");
            System.out.println("3. Update Lane");
            System.out.println("4. Open/Close Lane");
            System.out.println("5. View Available Lanes");
            System.out.println("6. View Lane Utilization");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> addLane();
                    case 2 -> viewAllLanes();
                    case 3 -> updateLane();
                    case 4 -> toggleLaneStatus();
                    case 5 -> viewAvailableLanes();
                    case 6 -> viewLaneUtilization();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private void addLane() {
        System.out.println("\n--- Add Gate Lane ---");

        try {
            System.out.print("Enter lane ID: ");
            int id = scanner.nextInt();

            System.out.print("Enter lane number: ");
            int laneNumber = scanner.nextInt();

            System.out.print("Enter capacity per minute: ");
            int capacity = scanner.nextInt();
            scanner.nextLine();

            laneService.addLane(id, laneNumber, capacity);
            System.out.println("Lane added successfully.");
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void viewAllLanes() {
        System.out.println("\n--- All Gate Lanes ---");
        List<GateLane> lanes = laneService.getAllLanes();

        if (lanes.isEmpty()) {
            System.out.println("No lanes found.");
        } else {
            for (GateLane lane : lanes) {
                System.out.printf("Lane %d: Status=%s, Capacity=%d, Current Load=%d, Utilization=%.1f%%\n",
                        lane.getLaneNumber(), lane.getStatus(),
                        lane.getCapacityPerMinute(), lane.getCurrentLoad(),
                        laneService.getLaneUtilization(lane.getId()));
            }
        }
    }

    private void showTrafficMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== TRAFFIC MANAGEMENT ===");
            System.out.println("1. Request Entry (Resident)");
            System.out.println("2. Request Exit (Resident)");
            System.out.println("3. Request Visitor Entry");
            System.out.println("4. View Pending Passes");
            System.out.println("5. Approve/Deny Pass");
            System.out.println("6. Complete Pass");
            System.out.println("7. View Traffic Logs");
            System.out.println("8. View Lane Traffic");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> requestEntry();
                    case 2 -> requestExit();
                    case 3 -> requestVisitorEntry();
                    case 4 -> viewPendingPasses();
                    case 5 -> processPass();
                    case 6 -> completePass();
                    case 7 -> viewTrafficLogs();
                    case 8 -> viewLaneTraffic();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private void requestEntry() {
        System.out.println("\n--- Request Entry ---");

        try {
            System.out.print("Enter vehicle ID: ");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            GatePass pass = trafficService.requestEntry(vehicleId);
            System.out.println("Entry request created: " + pass);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void viewPendingPasses() {
        System.out.println("\n--- Pending Gate Passes ---");
        List<GatePass> pending = trafficService.getPendingPasses();

        if (pending.isEmpty()) {
            System.out.println("No pending passes.");
        } else {
            for (GatePass pass : pending) {
                System.out.println(pass);
            }
        }
    }

    private void processPass() {
        System.out.println("\n--- Process Gate Pass ---");

        try {
            System.out.print("Enter pass ID: ");
            int passId = scanner.nextInt();
            scanner.nextLine();

            System.out.println("1. Approve");
            System.out.println("2. Deny");
            System.out.print("Select action: ");
            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 1 -> {
                    GatePass approved = trafficService.approvePass(passId);
                    System.out.println("Pass approved: " + approved);
                }
                case 2 -> {
                    GatePass denied = trafficService.denyPass(passId);
                    System.out.println("Pass denied: " + denied);
                }
                default -> System.out.println("Invalid action.");
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void showVisitorMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== VISITOR MANAGEMENT ===");
            System.out.println("1. Create Visit Reservation");
            System.out.println("2. View All Reservations");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. Validate Visitor Access");
            System.out.println("5. View Active Reservations");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> createReservation();
                    case 2 -> viewAllReservations();
                    case 3 -> cancelReservation();
                    case 4 -> validateVisitorAccess();
                    case 5 -> viewActiveReservations();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            } catch (Exception e) {
                handleException(e);
            }
        }
    }
// Add these methods to your ConsoleUI class

    private void updateVehicle() {
        System.out.println("\n--- Update Vehicle ---");

        try {
            System.out.print("Enter vehicle ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter new plate number: ");
            String plate = scanner.nextLine();

            System.out.print("Enter new owner resident ID: ");
            int ownerId = scanner.nextInt();

            System.out.println("Available vehicle types:");
            System.out.println("1. Car");
            System.out.println("2. SUV");
            System.out.println("3. Motorcycle");
            System.out.print("Select vehicle type (1-3): ");
            int typeChoice = scanner.nextInt();

            System.out.print("Is vehicle allowed? (true/false): ");
            boolean allowed = scanner.nextBoolean();
            scanner.nextLine();

            Vehicle updated = vehicleService.updateVehicle(id, plate, ownerId, typeChoice, allowed);
            System.out.println("Vehicle updated successfully: " + updated);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void deleteVehicle() {
        System.out.println("\n--- Delete Vehicle ---");

        try {
            System.out.print("Enter vehicle ID to delete: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Are you sure? (yes/no): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("yes")) {
                vehicleService.deleteVehicle(id);
                System.out.println("Vehicle deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void searchVehicleByPlate() {
        System.out.println("\n--- Search Vehicle by Plate ---");

        try {
            System.out.print("Enter plate number: ");
            String plate = scanner.nextLine();

            java.util.Optional<Vehicle> vehicle = vehicleService.findVehicleByPlate(plate);

            if (vehicle.isPresent()) {
                System.out.println("Vehicle found: " + vehicle.get());
            } else {
                System.out.println("No vehicle found with plate: " + plate);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void toggleVehicleAccess() {
        System.out.println("\n--- Toggle Vehicle Access ---");

        try {
            System.out.print("Enter vehicle ID: ");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Allow access? (true/false): ");
            boolean allowed = scanner.nextBoolean();
            scanner.nextLine();

            vehicleService.toggleVehicleAccess(vehicleId, allowed);
            System.out.println("Vehicle access updated successfully.");
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void updateLane() {
        System.out.println("\n--- Update Gate Lane ---");

        try {
            System.out.print("Enter lane ID to update: ");
            int id = scanner.nextInt();

            System.out.print("Enter new lane number: ");
            int laneNumber = scanner.nextInt();

            System.out.print("Enter new capacity per minute: ");
            int capacity = scanner.nextInt();
            scanner.nextLine();

            GateLane updated = laneService.updateLane(id, laneNumber, capacity);
            System.out.println("Lane updated successfully: " + updated);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void toggleLaneStatus() {
        System.out.println("\n--- Open/Close Lane ---");

        try {
            System.out.print("Enter lane ID: ");
            int laneId = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Select new status:");
            System.out.println("1. OPEN");
            System.out.println("2. CLOSED");
            System.out.println("3. BUSY");
            System.out.print("Enter choice (1-3): ");
            int statusChoice = scanner.nextInt();
            scanner.nextLine();

            LaneStatus status;
            switch (statusChoice) {
                case 1 -> status = LaneStatus.OPEN;
                case 2 -> status = LaneStatus.CLOSED;
                case 3 -> status = LaneStatus.BUSY;
                default -> {
                    System.out.println("Invalid choice. Using OPEN as default.");
                    status = LaneStatus.OPEN;
                }
            }

            GateLane updated = laneService.updateLaneStatus(laneId, status);
            System.out.println("Lane status updated: " + updated);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void viewAvailableLanes() {
        System.out.println("\n--- Available Lanes ---");
        List<GateLane> availableLanes = laneService.getAvailableLanes();

        if (availableLanes.isEmpty()) {
            System.out.println("No lanes currently available.");
        } else {
            System.out.println("Available lanes:");
            for (GateLane lane : availableLanes) {
                System.out.printf("Lane %d: Capacity %d/%d (%.1f%% utilized)\n",
                        lane.getLaneNumber(), lane.getCurrentLoad(),
                        lane.getCapacityPerMinute(),
                        laneService.getLaneUtilization(lane.getId()));
            }
            System.out.println("Total available: " + availableLanes.size() + " lanes");
        }
    }

    private void viewLaneUtilization() {
        System.out.println("\n--- Lane Utilization ---");
        List<GateLane> lanes = laneService.getAllLanes();

        if (lanes.isEmpty()) {
            System.out.println("No lanes found.");
        } else {
            System.out.println("Lane Utilization Report:");
            System.out.println("+-------+------------+-----------------+--------------+");
            System.out.println("| Lane  | Status     | Current/Capacity| Utilization  |");
            System.out.println("+-------+------------+-----------------+--------------+");

            for (GateLane lane : lanes) {
                double utilization = laneService.getLaneUtilization(lane.getId());
                String statusColor = "";
                String resetColor = "";

                // For console coloring (optional)
                if (utilization > 90) statusColor = "\u001B[31m"; // Red
                else if (utilization > 70) statusColor = "\u001B[33m"; // Yellow
                else statusColor = "\u001B[32m"; // Green

                System.out.printf("| %-5d | %-10s | %-15s | %s%-10.1f%%%s |\n",
                        lane.getLaneNumber(),
                        lane.getStatus(),
                        lane.getCurrentLoad() + "/" + lane.getCapacityPerMinute(),
                        statusColor, utilization, resetColor);
            }
            System.out.println("+-------+------------+-----------------+--------------+");
        }
    }

    private void requestExit() {
        System.out.println("\n--- Request Exit ---");

        try {
            System.out.print("Enter vehicle ID: ");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            GatePass pass = trafficService.requestExit(vehicleId);
            System.out.println("Exit request created: " + pass);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void requestVisitorEntry() {
        System.out.println("\n--- Request Visitor Entry ---");

        try {
            System.out.print("Enter visitor vehicle plate number: ");
            String plate = scanner.nextLine();

            GatePass pass = trafficService.requestVisitorEntry(plate);
            System.out.println("Visitor entry request created: " + pass);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void completePass() {
        System.out.println("\n--- Complete Gate Pass ---");

        try {
            System.out.print("Enter pass ID to complete: ");
            int passId = scanner.nextInt();
            scanner.nextLine();

            GatePass completed = trafficService.completePass(passId);
            System.out.println("Pass completed: " + completed);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void viewTrafficLogs() {
        System.out.println("\n--- Traffic Logs ---");
        List<GatePass> allPasses = trafficService.getAllPasses();

        if (allPasses.isEmpty()) {
            System.out.println("No traffic logs found.");
        } else {
            System.out.println("Traffic Logs (Most recent first):");
            // Sort by pass time descending
            allPasses.sort((p1, p2) -> p2.getPassTime().compareTo(p1.getPassTime()));

            for (GatePass pass : allPasses) {
                System.out.println(pass);
            }
            System.out.println("Total passes: " + allPasses.size());
        }
    }

    private void viewLaneTraffic() {
        System.out.println("\n--- Lane Traffic ---");

        try {
            System.out.print("Enter lane ID to view traffic: ");
            int laneId = scanner.nextInt();
            scanner.nextLine();

            List<GatePass> lanePasses = trafficService.getPassesByLane(laneId);

            if (lanePasses.isEmpty()) {
                System.out.println("No traffic recorded for this lane.");
            } else {
                System.out.println("Traffic for Lane " + laneId + ":");
                for (GatePass pass : lanePasses) {
                    System.out.println(pass);
                }
                System.out.println("Total passes: " + lanePasses.size());
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void cancelReservation() {
        System.out.println("\n--- Cancel Visit Reservation ---");

        try {
            System.out.print("Enter reservation ID to cancel: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Are you sure? (yes/no): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("yes")) {
                visitorService.cancelReservation(id);
                System.out.println("Reservation cancelled successfully.");
            } else {
                System.out.println("Cancellation cancelled.");
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void validateVisitorAccess() {
        System.out.println("\n--- Validate Visitor Access ---");

        try {
            System.out.print("Enter visitor vehicle plate number: ");
            String plate = scanner.nextLine();

            boolean isValid = visitorService.validateVisitorAccess(plate);

            if (isValid) {
                System.out.println("✓ Access GRANTED: Visitor has valid reservation.");
            } else {
                System.out.println("✗ Access DENIED: No valid reservation found or reservation expired.");
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void viewActiveReservations() {
        System.out.println("\n--- Active Visit Reservations ---");
        List<VisitReservation> activeReservations = visitorService.getActiveReservations();

        if (activeReservations.isEmpty()) {
            System.out.println("No active reservations found.");
        } else {
            System.out.println("Active Reservations:");
            for (VisitReservation reservation : activeReservations) {
                System.out.println(reservation);
            }
            System.out.println("Total active: " + activeReservations.size() + " reservations");
        }
    }
    private void createReservation() {
        System.out.println("\n--- Create Visit Reservation ---");

        try {
            System.out.print("Enter reservation ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter visitor name: ");
            String name = scanner.nextLine();

            System.out.print("Enter vehicle plate: ");
            String plate = scanner.nextLine();

            System.out.print("Enter visit date (yyyy-mm-dd): ");
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);

            System.out.print("Enter visit time (HH:mm): ");
            String timeStr = scanner.nextLine();
            LocalTime time = LocalTime.parse(timeStr, timeFormatter);

            System.out.print("Enter number of passengers: ");
            int passengers = scanner.nextInt();
            scanner.nextLine();

            VisitReservation reservation = visitorService.createReservation(
                    id, name, plate, date, time, passengers);
            System.out.println("Reservation created: " + reservation);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date/time format. Please use yyyy-mm-dd and HH:mm.");
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void viewAllReservations() {
        System.out.println("\n--- All Visit Reservations ---");
        List<VisitReservation> reservations = visitorService.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (VisitReservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    private void showDriverMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== DRIVER OPERATIONS ===");
            System.out.println("1. Request Entry (Resident)");
            System.out.println("2. Request Exit (Resident)");
            System.out.println("3. Request Visitor Entry");
            System.out.println("4. Check Lane Availability");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> requestEntry();
                    case 2 -> requestExit();
                    case 3 -> requestVisitorEntry();
                    case 4 -> checkLaneAvailability();
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private void checkLaneAvailability() {
        System.out.println("\n--- Lane Availability ---");
        List<GateLane> available = laneService.getAvailableLanes();

        if (available.isEmpty()) {
            System.out.println("No lanes currently available.");
        } else {
            System.out.println("Available lanes:");
            for (GateLane lane : available) {
                System.out.printf("Lane %d: Capacity %d/%d (%.1f%% utilized)\n",
                        lane.getLaneNumber(), lane.getCurrentLoad(),
                        lane.getCapacityPerMinute(),
                        laneService.getLaneUtilization(lane.getId()));
            }
        }
    }

    private void handleException(Exception e) {
        if (e instanceof CompoundGateException) {
            System.out.println("Error: " + e.getMessage());
        } else if (e instanceof InputMismatchException) {
            System.out.println("Invalid input format. Please try again.");
            scanner.nextLine(); // Clear invalid input
        } else {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace(); // For debugging in development
        }
    }

    // Other methods for updateVehicle, deleteVehicle, etc. follow similar pattern
    // Due to space constraints, I'm showing the pattern. All methods would follow similar structure.
}