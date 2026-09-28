package com.urbantraffic.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
@Table(name = "toll_rates")
public class TollRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toll_plaza_id", nullable = false)
    private TollPlaza tollPlaza;

    @Enumerated(EnumType.STRING)
    @NotNull
    private VehicleType vehicleType;

    @Positive
    private double baseRate;

    @Positive
    private double optimizedRate;

    @NotNull
    private LocalDateTime timestamp;

    public TollRate() {
    }

    public TollRate(TollPlaza tollPlaza, VehicleType vehicleType, double baseRate, double optimizedRate, LocalDateTime timestamp) {
        this.tollPlaza = tollPlaza;
        this.vehicleType = vehicleType;
        this.baseRate = baseRate;
        this.optimizedRate = optimizedRate;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TollPlaza getTollPlaza() {
        return tollPlaza;
    }

    public void setTollPlaza(TollPlaza tollPlaza) {
        this.tollPlaza = tollPlaza;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public double getOptimizedRate() {
        return optimizedRate;
    }

    public void setOptimizedRate(double optimizedRate) {
        this.optimizedRate = optimizedRate;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
