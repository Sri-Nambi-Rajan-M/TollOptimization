package com.urbantraffic.platform.repository;

import com.urbantraffic.platform.entity.CongestionLevel;
import com.urbantraffic.platform.entity.Road;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadRepository extends JpaRepository<Road, Long> {
    List<Road> findBySourceAndDestination(String source, String destination);
    List<Road> findByCurrentStatus(CongestionLevel status);
    List<Road> findBySource(String source);
    List<Road> findByDestination(String destination);
}
