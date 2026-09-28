package com.urbantraffic.platform.controller;

import com.urbantraffic.platform.algorithm.RouteOptimizationService;
import com.urbantraffic.platform.entity.VehicleType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RouteController {
    private final RouteOptimizationService routeOptimizationService;

    public RouteController(RouteOptimizationService routeOptimizationService) {
        this.routeOptimizationService = routeOptimizationService;
    }

    @GetMapping("/routes/search")
    public ResponseEntity<List<RouteOptimizationService.RouteResult>> searchRoutes(@RequestParam String source,
                                                                                 @RequestParam String destination,
                                                                                 @RequestParam VehicleType vehicleType) {
        List<RouteOptimizationService.RouteResult> routes = routeOptimizationService.findRoutes(source, destination, vehicleType);
        return ResponseEntity.ok(routes);
    }
}
