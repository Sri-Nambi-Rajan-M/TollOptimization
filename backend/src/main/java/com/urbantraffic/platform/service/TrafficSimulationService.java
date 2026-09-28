package com.urbantraffic.platform.service;

import com.urbantraffic.platform.algorithm.CongestionCalculator;
import com.urbantraffic.platform.entity.*;
import com.urbantraffic.platform.repository.RoadRepository;
import com.urbantraffic.platform.repository.TrafficRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class TrafficSimulationService {
    private final RoadRepository roadRepository;
    private final TrafficRecordRepository trafficRecordRepository;
    private final Random random = new Random();

    public TrafficSimulationService(RoadRepository roadRepository, TrafficRecordRepository trafficRecordRepository) {
        this.roadRepository = roadRepository;
        this.trafficRecordRepository = trafficRecordRepository;
    }

    public List<TrafficRecord> generateTrafficForAllRoads() {
        List<Road> roads = roadRepository.findAll();
        for (Road road : roads) {
            generateTrafficRecord(road, randomCongestionLevel(), LocalDateTime.now());
        }
        return trafficRecordRepository.findAll();
    }

    public TrafficRecord generateTrafficRecord(Road road, CongestionLevel level, LocalDateTime timestamp) {
        int lowerBound;
        int upperBound;
        double speedMin;
        double speedMax;

        switch (level) {
            case LOW -> {
                lowerBound = (int) Math.round(road.getCapacity() * 0.2);
                upperBound = (int) Math.round(road.getCapacity() * 0.4);
                speedMin = road.getNormalSpeed() * 0.7;
                speedMax = road.getNormalSpeed() * 1.0;
            }
            case MODERATE -> {
                lowerBound = (int) Math.round(road.getCapacity() * 0.4);
                upperBound = (int) Math.round(road.getCapacity() * 0.6);
                speedMin = road.getNormalSpeed() * 0.5;
                speedMax = road.getNormalSpeed() * 0.7;
            }
            case HIGH -> {
                lowerBound = (int) Math.round(road.getCapacity() * 0.6);
                upperBound = (int) Math.round(road.getCapacity() * 0.8);
                speedMin = road.getNormalSpeed() * 0.3;
                speedMax = road.getNormalSpeed() * 0.5;
            }
            case SEVERE -> {
                lowerBound = (int) Math.round(road.getCapacity() * 0.8);
                upperBound = (int) Math.round(road.getCapacity() * 1.0);
                speedMin = road.getNormalSpeed() * 0.1;
                speedMax = road.getNormalSpeed() * 0.3;
            }
            default -> {
                lowerBound = 0;
                upperBound = road.getCapacity();
                speedMin = road.getNormalSpeed() * 0.5;
                speedMax = road.getNormalSpeed() * 1.0;
            }
        }

        int vehicleCount = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
        double averageSpeed = speedMin + (speedMax - speedMin) * random.nextDouble();
        double travelTime = road.getDistance() / Math.max(averageSpeed, 1.0);
        double congestionScore = CongestionCalculator.calculateCongestionScore(road, new TrafficRecord(road, vehicleCount, averageSpeed, travelTime, 0, level, timestamp));

        TrafficRecord record = new TrafficRecord();
        record.setRoad(road);
        record.setVehicleCount(vehicleCount);
        record.setAverageSpeed(averageSpeed);
        record.setTravelTime(travelTime);
        record.setCongestionScore(congestionScore);
        record.setCongestionLevel(level);
        record.setTimestamp(timestamp);

        road.setCurrentStatus(level);
        roadRepository.save(road);
        return trafficRecordRepository.save(record);
    }

    public CongestionLevel randomCongestionLevel() {
        int choice = random.nextInt(100);
        if (choice < 35) return CongestionLevel.LOW;
        if (choice < 60) return CongestionLevel.MODERATE;
        if (choice < 85) return CongestionLevel.HIGH;
        return CongestionLevel.SEVERE;
    }
}
