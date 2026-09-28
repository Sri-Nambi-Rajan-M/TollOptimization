package com.urbantraffic.platform.service;

import com.urbantraffic.platform.entity.*;
import com.urbantraffic.platform.exception.ApiException;
import com.urbantraffic.platform.repository.TollPlazaRepository;
import com.urbantraffic.platform.repository.TollRateRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TollOptimizationService {
    private final TollPlazaRepository tollPlazaRepository;
    private final TollRateRepository tollRateRepository;

    public TollOptimizationService(TollPlazaRepository tollPlazaRepository, TollRateRepository tollRateRepository) {
        this.tollPlazaRepository = tollPlazaRepository;
        this.tollRateRepository = tollRateRepository;
    }

    public double calculateOptimizedToll(TollPlaza plaza, VehicleType vehicleType, CongestionLevel congestionLevel, boolean peakHour) {
        double base = plaza.getBaseToll();
        double surchargePercent = switch (congestionLevel) {
            case LOW -> 0;
            case MODERATE -> 5;
            case HIGH -> 10;
            case SEVERE -> 20;
        };

        if (peakHour) {
            surchargePercent += 5;
        }

        double adjusted = base * (1 + (surchargePercent / 100.0));

        switch (vehicleType) {
            case BIKE -> adjusted *= 0.6;
            case BUS -> adjusted *= 1.4;
            case TRUCK -> adjusted *= 2.0;
            default -> {
            }
        }

        double finalRate = Math.max(plaza.getMinimumToll(), Math.min(plaza.getMaximumToll(), Math.round(adjusted)));
        return finalRate;
    }

    public TollRate optimizeForPlaza(Long tollPlazaId, VehicleType vehicleType, CongestionLevel congestionLevel) {
        TollPlaza plaza = tollPlazaRepository.findById(tollPlazaId)
            .orElseThrow(() -> new ApiException("Toll plaza not found", HttpStatus.NOT_FOUND));
        boolean isPeak = isPeakHour(LocalDateTime.now());
        double optimized = calculateOptimizedToll(plaza, vehicleType, congestionLevel, isPeak);

        TollRate tollRate = new TollRate();
        tollRate.setTollPlaza(plaza);
        tollRate.setVehicleType(vehicleType);
        tollRate.setBaseRate(plaza.getBaseToll());
        tollRate.setOptimizedRate(optimized);
        tollRate.setTimestamp(LocalDateTime.now());
        return tollRateRepository.save(tollRate);
    }

    public boolean isPeakHour(LocalDateTime time) {
        int hour = time.getHour();
        return (hour >= 8 && hour < 10) || (hour >= 17 && hour < 20);
    }

    public List<TollRate> getAllRates() {
        return tollRateRepository.findAll();
    }
}
