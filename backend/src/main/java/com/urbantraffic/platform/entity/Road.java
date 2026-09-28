package com.urbantraffic.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roads")
public class Road {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String source;

    @NotBlank
    private String destination;

    @Positive
    private double distance;

    @Positive
    private int capacity;

    @Positive
    private double normalSpeed;

    @Enumerated(EnumType.STRING)
    private CongestionLevel currentStatus;

    @OneToMany(mappedBy = "road", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TrafficRecord> trafficRecords = new ArrayList<>();

    @OneToMany(mappedBy = "road", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Incident> incidents = new ArrayList<>();

    @OneToMany(mappedBy = "road", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TollPlaza> tollPlazas = new ArrayList<>();

    public Road() {
    }

    public Road(String name, String source, String destination, double distance, int capacity, double normalSpeed, CongestionLevel currentStatus) {
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.capacity = capacity;
        this.normalSpeed = normalSpeed;
        this.currentStatus = currentStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getNormalSpeed() {
        return normalSpeed;
    }

    public void setNormalSpeed(double normalSpeed) {
        this.normalSpeed = normalSpeed;
    }

    public CongestionLevel getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(CongestionLevel currentStatus) {
        this.currentStatus = currentStatus;
    }

    public List<TrafficRecord> getTrafficRecords() {
        return trafficRecords;
    }

    public void setTrafficRecords(List<TrafficRecord> trafficRecords) {
        this.trafficRecords = trafficRecords;
    }

    public List<Incident> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<Incident> incidents) {
        this.incidents = incidents;
    }

    public List<TollPlaza> getTollPlazas() {
        return tollPlazas;
    }

    public void setTollPlazas(List<TollPlaza> tollPlazas) {
        this.tollPlazas = tollPlazas;
    }
}
