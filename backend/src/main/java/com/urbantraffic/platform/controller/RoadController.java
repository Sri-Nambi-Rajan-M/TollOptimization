package com.urbantraffic.platform.controller;

import com.urbantraffic.platform.entity.CongestionLevel;
import com.urbantraffic.platform.entity.Road;
import com.urbantraffic.platform.exception.ApiException;
import com.urbantraffic.platform.repository.RoadRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoadController {
    private final RoadRepository roadRepository;

    public RoadController(RoadRepository roadRepository) {
        this.roadRepository = roadRepository;
    }

    @GetMapping("/roads")
    public List<Road> getAllRoads() {
        return roadRepository.findAll();
    }

    @GetMapping("/roads/{id}")
    public Road getRoadById(@PathVariable Long id) {
        return roadRepository.findById(id)
                .orElseThrow(() -> new ApiException("Road not found", HttpStatus.NOT_FOUND));
    }

    @PostMapping("/roads")
    public ResponseEntity<Road> createRoad(@Valid @RequestBody Road road) {
        if (road.getCurrentStatus() == null) {
            road.setCurrentStatus(CongestionLevel.LOW);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roadRepository.save(road));
    }

    @PutMapping("/roads/{id}")
    public Road updateRoad(@PathVariable Long id, @Valid @RequestBody Road updatedRoad) {
        Road road = roadRepository.findById(id)
                .orElseThrow(() -> new ApiException("Road not found", HttpStatus.NOT_FOUND));
        road.setName(updatedRoad.getName());
        road.setSource(updatedRoad.getSource());
        road.setDestination(updatedRoad.getDestination());
        road.setDistance(updatedRoad.getDistance());
        road.setCapacity(updatedRoad.getCapacity());
        road.setNormalSpeed(updatedRoad.getNormalSpeed());
        road.setCurrentStatus(updatedRoad.getCurrentStatus() != null ? updatedRoad.getCurrentStatus() : CongestionLevel.LOW);
        return roadRepository.save(road);
    }

    @DeleteMapping("/roads/{id}")
    public ResponseEntity<Void> deleteRoad(@PathVariable Long id) {
        Road road = roadRepository.findById(id)
                .orElseThrow(() -> new ApiException("Road not found", HttpStatus.NOT_FOUND));
        roadRepository.delete(road);
        return ResponseEntity.noContent().build();
    }
}
