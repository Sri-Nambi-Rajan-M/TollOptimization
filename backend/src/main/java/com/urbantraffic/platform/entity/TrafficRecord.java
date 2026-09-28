package com.urbantraffic.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

@Entity
@Table(name = "traffic_records")
public class TrafficRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "road_id", nullable = false)
    private Road road;

    @Positive
    private int vehicleCount;

    @Positive
    private double averageSpeed;

    @Positive
    private double travelTime;

    @PositiveOrZero
    private double congestionScore;

    @Enumerated(EnumType.STRING)
    private CongestionLevel congestionLevel;

    @NotNull
    private LocalDateTime timestamp;

    public TrafficRecord() {
    }

    public TrafficRecord(Road road, int vehicleCount, double averageSpeed, double travelTime, double congestionScore, CongestionLevel congestionLevel, LocalDateTime timestamp) {
        this.road = road;
        this.vehicleCount = vehicleCount;
        this.averageSpeed = averageSpeed;
        this.travelTime = travelTime;
        this.congestionScore = congestionScore;
        this.congestionLevel = congestionLevel;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(int vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }

    public double getCongestionScore() {
        return congestionScore;
    }

    public void setCongestionScore(double congestionScore) {
        this.congestionScore = congestionScore;
    }

    public CongestionLevel getCongestionLevel() {
        return congestionLevel;
    }

    public void setCongestionLevel(CongestionLevel congestionLevel) {
        this.congestionLevel = congestionLevel;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
