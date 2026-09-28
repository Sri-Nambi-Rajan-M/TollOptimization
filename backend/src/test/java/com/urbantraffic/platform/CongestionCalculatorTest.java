package com.urbantraffic.platform;

import com.urbantraffic.platform.algorithm.CongestionCalculator;
import com.urbantraffic.platform.entity.CongestionLevel;
import com.urbantraffic.platform.entity.Road;
import com.urbantraffic.platform.entity.TrafficRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CongestionCalculatorTest {

    @Test
    void shouldClassifyLowCongestion() {
        Road road = new Road("A", "X", "Y", 10, 100, 50, CongestionLevel.LOW);
        TrafficRecord record = new TrafficRecord(road, 20, 45, 0.2, 0, CongestionLevel.LOW, LocalDateTime.now());
        double score = CongestionCalculator.calculateCongestionScore(road, record);
        assertEquals(CongestionLevel.LOW, CongestionCalculator.classify(score));
    }

    @Test
    void shouldClassifySevereCongestion() {
        Road road = new Road("B", "Y", "Z", 10, 100, 50, CongestionLevel.SEVERE);
        TrafficRecord record = new TrafficRecord(road, 95, 8, 7, 0, CongestionLevel.SEVERE, LocalDateTime.now());
        double score = CongestionCalculator.calculateCongestionScore(road, record);
        assertEquals(CongestionLevel.SEVERE, CongestionCalculator.classify(score));
    }
}
