package com.urbantraffic.platform.algorithm;

import com.urbantraffic.platform.entity.CongestionLevel;
import com.urbantraffic.platform.entity.Road;
import com.urbantraffic.platform.entity.TrafficRecord;

import java.util.List;

public class CongestionCalculator {

    /**
     * Weighted congestion formula:
     * score = (trafficUtilization * 40) + (speedReduction * 30) + (timeFactor * 30)
     * where trafficUtilization = vehicleCount / capacity
     * speedReduction = 1 - (averageSpeed / normalSpeed)
     * timeFactor = min(travelTime / expectedTravelTime, 1.0)
     *
     * This keeps the logic simple while still producing a practical congestion score from 0 to 100.
     */
    public static double calculateCongestionScore(Road road, TrafficRecord record) {
        double trafficUtilization = Math.min(record.getVehicleCount() / (double) Math.max(road.getCapacity(), 1), 1.0);
        double speedReduction = Math.max(1 - (record.getAverageSpeed() / Math.max(road.getNormalSpeed(), 1)), 0);
        double expectedTravelTime = Math.max(road.getDistance() / Math.max(road.getNormalSpeed(), 1), 0.1);
        double timeRatio = Math.max(record.getTravelTime() / Math.max(expectedTravelTime, 0.1), 0);
        double timeFactor = Math.min(timeRatio - 1, 1.0);
        if (timeFactor < 0) {
            timeFactor = 0;
        }

        double normalized = (trafficUtilization * 0.40) + (speedReduction * 0.30) + (timeFactor * 0.30);
        return Math.min(Math.max(normalized * 100, 0), 100);
    }

    public static CongestionLevel classify(double score) {
        if (score <= 25) return CongestionLevel.LOW;
        if (score <= 50) return CongestionLevel.MODERATE;
        if (score <= 75) return CongestionLevel.HIGH;
        return CongestionLevel.SEVERE;
    }

    public static double calculateAverageCongestion(List<TrafficRecord> records) {
        if (records == null || records.isEmpty()) {
            return 0;
        }

        double total = 0;
        for (TrafficRecord record : records) {
            total += record.getCongestionScore();
        }
        return total / records.size();
    }
}
