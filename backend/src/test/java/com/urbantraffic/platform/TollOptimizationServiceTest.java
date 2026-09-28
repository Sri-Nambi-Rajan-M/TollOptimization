package com.urbantraffic.platform;

import com.urbantraffic.platform.entity.CongestionLevel;
import com.urbantraffic.platform.entity.Road;
import com.urbantraffic.platform.entity.TollPlaza;
import com.urbantraffic.platform.entity.VehicleType;
import com.urbantraffic.platform.service.TollOptimizationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TollOptimizationServiceTest {

    @Test
    void shouldOptimizeHighCongestionToll() {
        TollOptimizationService service = new TollOptimizationService(null, null);
        Road road = new Road("A", "X", "Y", 10, 100, 50, CongestionLevel.HIGH);
        TollPlaza plaza = new TollPlaza("P1", road, "Center", 50, 30, 150);
        double optimized = service.calculateOptimizedToll(plaza, VehicleType.CAR, CongestionLevel.HIGH, true);
        assertTrue(optimized >= 30);
        assertTrue(optimized <= 150);
    }

    @Test
    void shouldClampToMinimumToll() {
        TollOptimizationService service = new TollOptimizationService(null, null);
        Road road = new Road("B", "Y", "Z", 10, 100, 50, CongestionLevel.LOW);
        TollPlaza plaza = new TollPlaza("P2", road, "Center", 20, 35, 150);
        double optimized = service.calculateOptimizedToll(plaza, VehicleType.CAR, CongestionLevel.LOW, false);
        assertTrue(optimized >= 35);
    }
}
