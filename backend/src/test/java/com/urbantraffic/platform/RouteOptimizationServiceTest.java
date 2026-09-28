package com.urbantraffic.platform;

import com.urbantraffic.platform.algorithm.RouteOptimizationService;
import com.urbantraffic.platform.entity.CongestionLevel;
import com.urbantraffic.platform.entity.Road;
import com.urbantraffic.platform.entity.VehicleType;
import com.urbantraffic.platform.repository.RoadRepository;
import com.urbantraffic.platform.repository.TollPlazaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class RouteOptimizationServiceTest {

    @Test
    void shouldReturnSimpleRoutes() {
        RoadRepository roadRepository = Mockito.mock(RoadRepository.class);
        TollPlazaRepository tollPlazaRepository = Mockito.mock(TollPlazaRepository.class);

        Road road1 = new Road("A", "Chennai Central", "T Nagar", 10, 200, 50, CongestionLevel.LOW);
        Road road2 = new Road("B", "T Nagar", "Tambaram", 12, 200, 45, CongestionLevel.MODERATE);
        road1.setId(1L);
        road2.setId(2L);

        Mockito.when(roadRepository.findAll()).thenReturn(List.of(road1, road2));
        RouteOptimizationService service = new RouteOptimizationService(roadRepository, tollPlazaRepository);

        List<RouteOptimizationService.RouteResult> results = service.findRoutes("Chennai Central", "Tambaram", VehicleType.CAR);
        assertFalse(results.isEmpty());
    }
}
