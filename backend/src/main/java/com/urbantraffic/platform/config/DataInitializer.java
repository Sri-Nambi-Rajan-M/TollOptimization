package com.urbantraffic.platform.config;

import com.urbantraffic.platform.algorithm.CongestionCalculator;
import com.urbantraffic.platform.entity.*;
import com.urbantraffic.platform.repository.*;
import com.urbantraffic.platform.service.TrafficSimulationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoadRepository roadRepository;
    private final TollPlazaRepository tollPlazaRepository;
    private final TrafficRecordRepository trafficRecordRepository;
    private final IncidentRepository incidentRepository;
    private final PasswordEncoder passwordEncoder;
    private final TrafficSimulationService trafficSimulationService;

    public DataInitializer(
            UserRepository userRepository,
            RoadRepository roadRepository,
            TollPlazaRepository tollPlazaRepository,
            TrafficRecordRepository trafficRecordRepository,
            IncidentRepository incidentRepository,
            PasswordEncoder passwordEncoder,
            TrafficSimulationService trafficSimulationService) {
        this.userRepository = userRepository;
        this.roadRepository = roadRepository;
        this.tollPlazaRepository = tollPlazaRepository;
        this.trafficRecordRepository = trafficRecordRepository;
        this.incidentRepository = incidentRepository;
        this.passwordEncoder = passwordEncoder;
        this.trafficSimulationService = trafficSimulationService;
    }

    @Override
    public void run(String... args) {
        if (roadRepository.count() == 0) {
            createRoads();
        }
        createUsers();
        if (tollPlazaRepository.count() == 0) {
            createTollPlazas();
        }
        if (trafficRecordRepository.count() == 0) {
            createTrafficRecords();
        }
        if (incidentRepository.count() == 0) {
            createIncidents();
        }
    }

    private void createUsers() {
        if (!userRepository.existsByEmail("admin@example.com")) {
            userRepository.save(new UserEntity("Admin User", "admin@example.com", passwordEncoder.encode("admin123"), Role.ADMIN));
        }
        if (!userRepository.existsByEmail("user@example.com")) {
            userRepository.save(new UserEntity("Demo User", "user@example.com", passwordEncoder.encode("user123"), Role.USER));
        }
    }

    private void createRoads() {
        List<Road> roads = List.of(
                new Road("Chennai Central - T Nagar", "Chennai Central", "T Nagar", 12.5, 500, 55, CongestionLevel.MODERATE),
                new Road("T Nagar - Guindy", "T Nagar", "Guindy", 11.2, 420, 60, CongestionLevel.HIGH),
                new Road("Guindy - Tambaram", "Guindy", "Tambaram", 18.7, 600, 58, CongestionLevel.MODERATE),
                new Road("Chennai Central - Guindy", "Chennai Central", "Guindy", 15.5, 540, 62, CongestionLevel.LOW),
                new Road("T Nagar - Tambaram", "T Nagar", "Tambaram", 21.4, 700, 57, CongestionLevel.HIGH),
                new Road("Guindy - Airport", "Guindy", "Airport", 13.1, 460, 65, CongestionLevel.LOW),
                new Road("Airport - Tambaram", "Airport", "Tambaram", 17.8, 520, 60, CongestionLevel.MODERATE)
        );
        roadRepository.saveAll(roads);
    }

    private void createTollPlazas() {
        List<Road> roads = roadRepository.findAll();
        if (roads.size() >= 6) {
            TollPlaza tp1 = new TollPlaza("Central Toll Plaza", roads.get(0), "Chennai Central", 50, 30, 150);
            TollPlaza tp2 = new TollPlaza("Guindy Gate", roads.get(2), "Guindy", 70, 35, 180);
            TollPlaza tp3 = new TollPlaza("Airport Exit", roads.get(5), "Airport", 65, 30, 170);
            tollPlazaRepository.saveAll(List.of(tp1, tp2, tp3));
        }
    }

    private void createTrafficRecords() {
        List<Road> roads = roadRepository.findAll();
        for (Road road : roads) {
            for (int i = 0; i < 3; i++) {
                CongestionLevel level = switch (i) {
                    case 0 -> CongestionLevel.LOW;
                    case 1 -> CongestionLevel.MODERATE;
                    default -> CongestionLevel.HIGH;
                };
                TrafficRecord record = trafficSimulationService.generateTrafficRecord(road, level, LocalDateTime.now().minusHours(i + 1));
                record.setCongestionScore(CongestionCalculator.calculateCongestionScore(road, record));
                record.setCongestionLevel(level);
                trafficRecordRepository.save(record);
            }
        }
    }

    private void createIncidents() {
        List<Road> roads = roadRepository.findAll();
        if (roads.size() >= 6) {
            incidentRepository.saveAll(List.of(
                    new Incident(roads.get(1), IncidentType.ACCIDENT, "Minor accident near the midpoint", IncidentSeverity.HIGH, IncidentStatus.ACTIVE, LocalDateTime.now().minusHours(2)),
                    new Incident(roads.get(3), IncidentType.CONSTRUCTION, "Road maintenance work in progress", IncidentSeverity.MEDIUM, IncidentStatus.ACTIVE, LocalDateTime.now().minusHours(4)),
                    new Incident(roads.get(5), IncidentType.ROAD_BLOCK, "Temporary road block due to utility repair", IncidentSeverity.HIGH, IncidentStatus.RESOLVED, LocalDateTime.now().minusDays(1))
            ));
        }
    }
}
