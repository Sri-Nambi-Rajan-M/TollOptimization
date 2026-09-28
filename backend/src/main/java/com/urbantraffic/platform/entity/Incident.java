package com.urbantraffic.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "road_id", nullable = false)
    private Road road;

    @Enumerated(EnumType.STRING)
    @NotNull
    private IncidentType type;

    @NotBlank
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private IncidentSeverity severity;

    @Enumerated(EnumType.STRING)
    @NotNull
    private IncidentStatus status;

    @NotNull
    private LocalDateTime timestamp;

    public Incident() {
    }

    public Incident(Road road, IncidentType type, String description, IncidentSeverity severity, IncidentStatus status, LocalDateTime timestamp) {
        this.road = road;
        this.type = type;
        this.description = description;
        this.severity = severity;
        this.status = status;
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

    public IncidentType getType() {
        return type;
    }

    public void setType(IncidentType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IncidentSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(IncidentSeverity severity) {
        this.severity = severity;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
