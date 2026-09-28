package com.urbantraffic.platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "toll_plazas")
public class TollPlaza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "road_id", nullable = false)
    private Road road;

    @NotBlank
    private String location;

    @Positive
    private double baseToll;

    @Positive
    private double minimumToll;

    @Positive
    private double maximumToll;

    @OneToMany(mappedBy = "tollPlaza", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TollRate> tollRates = new ArrayList<>();

    public TollPlaza() {
    }

    public TollPlaza(String name, Road road, String location, double baseToll, double minimumToll, double maximumToll) {
        this.name = name;
        this.road = road;
        this.location = location;
        this.baseToll = baseToll;
        this.minimumToll = minimumToll;
        this.maximumToll = maximumToll;
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

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getBaseToll() {
        return baseToll;
    }

    public void setBaseToll(double baseToll) {
        this.baseToll = baseToll;
    }

    public double getMinimumToll() {
        return minimumToll;
    }

    public void setMinimumToll(double minimumToll) {
        this.minimumToll = minimumToll;
    }

    public double getMaximumToll() {
        return maximumToll;
    }

    public void setMaximumToll(double maximumToll) {
        this.maximumToll = maximumToll;
    }

    public List<TollRate> getTollRates() {
        return tollRates;
    }

    public void setTollRates(List<TollRate> tollRates) {
        this.tollRates = tollRates;
    }
}
