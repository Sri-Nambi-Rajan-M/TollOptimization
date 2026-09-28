package com.urbantraffic.platform.controller;

import com.urbantraffic.platform.algorithm.CongestionCalculator;
import com.urbantraffic.platform.dto.PredictionDto;
import com.urbantraffic.platform.dto.TrafficRecordDto;
import com.urbantraffic.platform.entity.CongestionLevel;
import com.urbantraffic.platform.entity.Road;
import com.urbantraffic.platform.entity.TrafficRecord;
import com.urbantraffic.platform.exception.ApiException;
import com.urbantraffic.platform.repository.RoadRepository;
import com.urbantraffic.platform.repository.TrafficRecordRepository;
import com.urbantraffic.platform.service.TrafficPredictionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TrafficController {
    private final TrafficRecordRepository trafficRecordRepository;
    private final RoadRepository roadRepository;
    private final TrafficPredictionService trafficPredictionService;

    public TrafficController(TrafficRecordRepository trafficRecordRepository,
                            RoadRepository roadRepository,
                            TrafficPredictionService trafficPredictionService) {
        this.trafficRecordRepository = trafficRecordRepository;
        this.roadRepository = roadRepository;
        this.trafficPredictionService = trafficPredictionService;
    }

    @GetMapping("/traffic")
    public List<TrafficRecord> getAllTraffic() {
        return trafficRecordRepository.findTop20ByOrderByTimestampDesc();
    }

    @GetMapping("/traffic/road/{roadId}")
    public List<TrafficRecord> getTrafficByRoad(@PathVariable Long roadId) {
        Road road = roadRepository.findById(roadId)
                .orElseThrow(() -> new ApiException("Road not found", HttpStatus.NOT_FOUND));
        return trafficRecordRepository.findByRoadOrderByTimestampDesc(road, PageRequest.of(0, 20));
    }

    @PostMapping("/traffic")
    public ResponseEntity<TrafficRecord> createTraffic(@Valid @RequestBody TrafficRecordDto dto) {
        Road road = roadRepository.findById(dto.getRoadId())
                .orElseThrow(() -> new ApiException("Road not found", HttpStatus.NOT_FOUND));

        double congestionScore = CongestionCalculator.calculateCongestionScore(road, new TrafficRecord(road, dto.getVehicleCount(), dto.getAverageSpeed(), dto.getTravelTime(), 0, CongestionLevel.LOW, LocalDateTime.now()));
        CongestionLevel level = CongestionCalculator.classify(congestionScore);

        TrafficRecord record = new TrafficRecord();
        record.setRoad(road);
        record.setVehicleCount(dto.getVehicleCount());
        record.setAverageSpeed(dto.getAverageSpeed());
        record.setTravelTime(dto.getTravelTime());
        record.setCongestionScore(congestionScore);
        record.setCongestionLevel(level);
        record.setTimestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now());

        road.setCurrentStatus(level);
        roadRepository.save(road);
        return ResponseEntity.status(HttpStatus.CREATED).body(trafficRecordRepository.save(record));
    }

    @GetMapping("/traffic/prediction/{roadId}")
    public PredictionDto getPrediction(@PathVariable Long roadId) {
        return trafficPredictionService.predictForRoad(roadId);
    }
}
