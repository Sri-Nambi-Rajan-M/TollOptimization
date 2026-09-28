package com.urbantraffic.platform.dto;

import com.urbantraffic.platform.entity.CongestionLevel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class TrafficRecordDto {
    private Long id;

    @NotNull
    private Long roadId;

    @Positive
    private int vehicleCount;

    @Positive
    private double averageSpeed;

    @Positive
    private double travelTime;

    private double congestionScore;

    private CongestionLevel congestionLevel;

    private LocalDateTime timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoadId() {
        return roadId;
    }

    public void setRoadId(Long roadId) {
        this.roadId = roadId;
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
