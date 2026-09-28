package com.urbantraffic.platform.repository;

import com.urbantraffic.platform.entity.Incident;
import com.urbantraffic.platform.entity.IncidentStatus;
import com.urbantraffic.platform.entity.Road;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByRoad(Road road);
    List<Incident> findByStatus(IncidentStatus status);
    List<Incident> findByRoadAndStatus(Road road, IncidentStatus status);
}
