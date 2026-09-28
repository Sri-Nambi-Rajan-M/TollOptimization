package com.urbantraffic.platform.service;

import com.urbantraffic.platform.algorithm.CongestionCalculator;
import com.urbantraffic.platform.dto.PredictionDto;
import com.urbantraffic.platform.entity.Road;
import com.urbantraffic.platform.entity.TrafficRecord;
import com.urbantraffic.platform.exception.ApiException;
import com.urbantraffic.platform.repository.RoadRepository;
import com.urbantraffic.platform.repository.TrafficRecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrafficPredictionService {
    private final RoadRepository roadRepository;
    private final TrafficRecordRepository trafficRecordRepository;

    public TrafficPredictionService(RoadRepository roadRepository, TrafficRecordRepository trafficRecordRepository) {
        this.roadRepository = roadRepository;
        this.trafficRecordRepository = trafficRecordRepository;
    }

    public PredictionDto predictForRoad(Long roadId) {
        Road road = roadRepository.findById(roadId)
                .orElseThrow(() -> new ApiException("Road not found", HttpStatus.NOT_FOUND));

        List<TrafficRecord> records = trafficRecordRepository.findByRoadOrderByTimestampDesc(road);
        if (records.isEmpty()) {
            throw new ApiException("No traffic history available for the selected road", HttpStatus.BAD_REQUEST);
        }

        int recentCount = Math.min(records.size(), 10);
        List<TrafficRecord> recentRecords = records.subList(0, recentCount);

        double avgVehicleCount = recentRecords.stream().mapToDouble(TrafficRecord::getVehicleCount).average().orElse(0);
        double avgSpeed = recentRecords.stream().mapToDouble(TrafficRecord::getAverageSpeed).average().orElse(0);
        double avgScore = recentRecords.stream().mapToDouble(TrafficRecord::getCongestionScore).average().orElse(0);

        PredictionDto dto = new PredictionDto();
        dto.setPredictedVehicleCount(Math.round(avgVehicleCount));
        dto.setPredictedAverageSpeed(Math.round(avgSpeed * 10.0) / 10.0);
        dto.setPredictedCongestionScore(Math.round(avgScore * 10.0) / 10.0);
        dto.setPredictedCongestionLevel(CongestionCalculator.classify(avgScore));
        return dto;
    }
}
