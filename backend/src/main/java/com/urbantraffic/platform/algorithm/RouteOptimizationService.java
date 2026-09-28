package com.urbantraffic.platform.algorithm;

import com.urbantraffic.platform.entity.*;
import com.urbantraffic.platform.repository.RoadRepository;
import com.urbantraffic.platform.repository.TollPlazaRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteOptimizationService {
    private final RoadRepository roadRepository;
    private final TollPlazaRepository tollPlazaRepository;

    public RouteOptimizationService(RoadRepository roadRepository, TollPlazaRepository tollPlazaRepository) {
        this.roadRepository = roadRepository;
        this.tollPlazaRepository = tollPlazaRepository;
    }

    public List<RouteResult> findRoutes(String source, String destination, VehicleType vehicleType) {
        if (roadRepository == null) {
            return List.of();
        }
        List<Road> roads = roadRepository.findAll();
        Map<String, List<Road>> graph = new HashMap<>();

        for (Road road : roads) {
            graph.computeIfAbsent(road.getSource(), k -> new ArrayList<>()).add(road);
            graph.computeIfAbsent(road.getDestination(), k -> new ArrayList<>()).add(road);
        }

        Map<String, Double> distanceTo = new HashMap<>();
        Map<String, Road> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(Node::cost));

        distanceTo.put(source, 0.0);
        priorityQueue.add(new Node(source, 0.0));

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();
            String node = current.name();
            if (visited.contains(node)) {
                continue;
            }
            visited.add(node);

            if (node.equals(destination)) {
                break;
            }

            for (Road road : graph.getOrDefault(node, List.of())) {
                String next = road.getSource().equals(node) ? road.getDestination() : road.getSource();
                if (next.equals(node)) {
                    continue;
                }
                if (visited.contains(next)) {
                    continue;
                }

                CongestionLevel congestion = road.getCurrentStatus() != null ? road.getCurrentStatus() : CongestionLevel.LOW;
                double congestionPenalty = switch (congestion) {
                    case LOW -> 0;
                    case MODERATE -> 5;
                    case HIGH -> 15;
                    case SEVERE -> 30;
                };

                double toll = calculateToll(road, vehicleType);
                double travelTime = road.getDistance() / Math.max(road.getNormalSpeed(), 1);
                double routeCost = travelTime + congestionPenalty + toll;
                double newCost = distanceTo.getOrDefault(node, Double.MAX_VALUE) + routeCost;

                if (newCost < distanceTo.getOrDefault(next, Double.MAX_VALUE)) {
                    distanceTo.put(next, newCost);
                    previous.put(next, road);
                    priorityQueue.add(new Node(next, newCost));
                }
            }
        }

        List<RouteResult> results = new ArrayList<>();
        Set<String> keys = new HashSet<>();
        for (Map.Entry<String, Double> entry : distanceTo.entrySet()) {
            String node = entry.getKey();
            if (node.equals(source) || node.equals(destination)) {
                continue;
            }
            if (!keys.add(node)) {
                continue;
            }
            Road road = previous.get(node);
            if (road != null) {
                var result = new RouteResult();
                result.setRouteName(road.getName());
                result.setSource(road.getSource());
                result.setDestination(road.getDestination());
                result.setDistance(road.getDistance());
                result.setEstimatedTravelTime(Math.max(road.getDistance() / road.getNormalSpeed(), 1));
                result.setToll(calculateToll(road, vehicleType));
                result.setCongestion(road.getCurrentStatus() != null ? road.getCurrentStatus() : CongestionLevel.LOW);
                result.setRouteCost(result.getEstimatedTravelTime() + congestionPenalty(road.getCurrentStatus()) + result.getToll());
                results.add(result);
            }
        }

        if (results.isEmpty()) {
            for (Road road : roads) {
                if (road.getSource().equals(source) && road.getDestination().equals(destination)) {
                    RouteResult result = new RouteResult();
                    result.setRouteName(road.getName());
                    result.setSource(road.getSource());
                    result.setDestination(road.getDestination());
                    result.setDistance(road.getDistance());
                    result.setEstimatedTravelTime(Math.max(road.getDistance() / road.getNormalSpeed(), 1));
                    result.setToll(calculateToll(road, vehicleType));
                    result.setCongestion(road.getCurrentStatus() != null ? road.getCurrentStatus() : CongestionLevel.LOW);
                    result.setRouteCost(result.getEstimatedTravelTime() + congestionPenalty(road.getCurrentStatus()) + result.getToll());
                    results.add(result);
                }
            }
        }

        results.sort(Comparator.comparingDouble(RouteResult::getRouteCost));
        return results;
    }

    private double calculateToll(Road road, VehicleType vehicleType) {
        double base = tollPlazaRepository.findByRoadId(road.getId()).stream()
                .mapToDouble(plaza -> plaza.getBaseToll())
                .findFirst()
                .orElse(50.0);

        switch (vehicleType) {
            case BIKE -> base *= 0.6;
            case BUS -> base *= 1.5;
            case TRUCK -> base *= 2.2;
            default -> {
            }
        }
        return Math.round(base);
    }

    private double congestionPenalty(CongestionLevel level) {
        if (level == null) return 0;
        return switch (level) {
            case LOW -> 0;
            case MODERATE -> 5;
            case HIGH -> 15;
            case SEVERE -> 30;
        };
    }

    public record Node(String name, double cost) {}

    public static class RouteResult {
        private String routeName;
        private String source;
        private String destination;
        private double distance;
        private double estimatedTravelTime;
        private double toll;
        private CongestionLevel congestion;
        private double routeCost;

        public String getRouteName() {
            return routeName;
        }

        public void setRouteName(String routeName) {
            this.routeName = routeName;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public double getEstimatedTravelTime() {
            return estimatedTravelTime;
        }

        public void setEstimatedTravelTime(double estimatedTravelTime) {
            this.estimatedTravelTime = estimatedTravelTime;
        }

        public double getToll() {
            return toll;
        }

        public void setToll(double toll) {
            this.toll = toll;
        }

        public CongestionLevel getCongestion() {
            return congestion;
        }

        public void setCongestion(CongestionLevel congestion) {
            this.congestion = congestion;
        }

        public double getRouteCost() {
            return routeCost;
        }

        public void setRouteCost(double routeCost) {
            this.routeCost = routeCost;
        }
    }
}
