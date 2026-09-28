package com.urbantraffic.platform.dto;

import com.urbantraffic.platform.entity.CongestionLevel;

public class PredictionDto {
    private double predictedVehicleCount;
    private double predictedAverageSpeed;
    private double predictedCongestionScore;
    private CongestionLevel predictedCongestionLevel;

    public double getPredictedVehicleCount() {
        return predictedVehicleCount;
    }

    public void setPredictedVehicleCount(double predictedVehicleCount) {
        this.predictedVehicleCount = predictedVehicleCount;
    }

    public double getPredictedAverageSpeed() {
        return predictedAverageSpeed;
    }

    public void setPredictedAverageSpeed(double predictedAverageSpeed) {
        this.predictedAverageSpeed = predictedAverageSpeed;
    }

    public double getPredictedCongestionScore() {
        return predictedCongestionScore;
    }

    public void setPredictedCongestionScore(double predictedCongestionScore) {
        this.predictedCongestionScore = predictedCongestionScore;
    }

    public CongestionLevel getPredictedCongestionLevel() {
        return predictedCongestionLevel;
    }

    public void setPredictedCongestionLevel(CongestionLevel predictedCongestionLevel) {
        this.predictedCongestionLevel = predictedCongestionLevel;
    }
}
