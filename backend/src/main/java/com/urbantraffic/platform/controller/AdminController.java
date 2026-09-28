package com.urbantraffic.platform.controller;

import com.urbantraffic.platform.entity.*;
import com.urbantraffic.platform.exception.ApiException;
import com.urbantraffic.platform.repository.IncidentRepository;
import com.urbantraffic.platform.repository.TollRateRepository;
import com.urbantraffic.platform.service.DashboardService;
import com.urbantraffic.platform.service.TollOptimizationService;
import com.urbantraffic.platform.service.TrafficSimulationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController {
    private final TrafficSimulationService trafficSimulationService;
    private final DashboardService dashboardService;
    private final TollOptimizationService tollOptimizationService;
    private final TollRateRepository tollRateRepository;
    private final IncidentRepository incidentRepository;

    public AdminController(TrafficSimulationService trafficSimulationService,
                          DashboardService dashboardService,
                          TollOptimizationService tollOptimizationService,
                          TollRateRepository tollRateRepository,
                          IncidentRepository incidentRepository) {
        this.trafficSimulationService = trafficSimulationService;
        this.dashboardService = dashboardService;
        this.tollOptimizationService = tollOptimizationService;
        this.tollRateRepository = tollRateRepository;
        this.incidentRepository = incidentRepository;
    }

    @PostMapping("/admin/simulation/run")
    public ResponseEntity<List<TrafficRecord>> runSimulation() {
        List<TrafficRecord> records = trafficSimulationService.generateTrafficForAllRoads();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/dashboard/summary")
    public ResponseEntity<?> getDashboardSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/tolls")
    public List<TollRate> getAllTollRates() {
        return tollRateRepository.findAll();
    }

    @GetMapping("/tolls/{id}")
    public TollRate getTollRateById(@PathVariable Long id) {
        return tollRateRepository.findById(id)
            .orElseThrow(() -> new ApiException("Toll rate not found", HttpStatus.NOT_FOUND));
    }

    @PostMapping("/tolls")
    public ResponseEntity<TollRate> createTollRate(@Valid @RequestBody TollRate tollRate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tollRateRepository.save(tollRate));
    }

    @PutMapping("/tolls/{id}")
    public TollRate updateTollRate(@PathVariable Long id, @Valid @RequestBody TollRate updated) {
        TollRate existing = tollRateRepository.findById(id)
            .orElseThrow(() -> new ApiException("Toll rate not found", HttpStatus.NOT_FOUND));
        existing.setVehicleType(updated.getVehicleType());
        existing.setBaseRate(updated.getBaseRate());
        existing.setOptimizedRate(updated.getOptimizedRate());
        existing.setTimestamp(LocalDateTime.now());
        return tollRateRepository.save(existing);
    }

    @PostMapping("/tolls/optimize")
    public ResponseEntity<TollRate> optimizeToll(@RequestParam Long tollPlazaId,
                                               @RequestParam VehicleType vehicleType,
                                               @RequestParam CongestionLevel congestionLevel) {
        TollRate tollRate = tollOptimizationService.optimizeForPlaza(tollPlazaId, vehicleType, congestionLevel);
        return ResponseEntity.ok(tollRate);
    }

    @GetMapping("/incidents")
    public List<Incident> getIncidents() {
        return incidentRepository.findAll();
    }

    @PostMapping("/incidents")
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) {
        incident.setTimestamp(LocalDateTime.now());
        if (incident.getStatus() == null) {
            incident.setStatus(IncidentStatus.ACTIVE);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(incidentRepository.save(incident));
    }

    @PutMapping("/incidents/{id}")
    public Incident updateIncident(@PathVariable Long id, @RequestBody Incident updated) {
        Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new ApiException("Incident not found", HttpStatus.NOT_FOUND));
        incident.setType(updated.getType());
        incident.setDescription(updated.getDescription());
        incident.setSeverity(updated.getSeverity());
        incident.setStatus(updated.getStatus());
        incident.setTimestamp(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

    @PutMapping("/incidents/{id}/resolve")
    public Incident resolveIncident(@PathVariable Long id) {
        Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new ApiException("Incident not found", HttpStatus.NOT_FOUND));
        incident.setStatus(IncidentStatus.RESOLVED);
        incident.setTimestamp(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

}
