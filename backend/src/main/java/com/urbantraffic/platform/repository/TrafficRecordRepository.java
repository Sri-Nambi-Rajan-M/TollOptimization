package com.urbantraffic.platform.repository;

import com.urbantraffic.platform.entity.Road;
import com.urbantraffic.platform.entity.TrafficRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrafficRecordRepository extends JpaRepository<TrafficRecord, Long> {
    List<TrafficRecord> findByRoadOrderByTimestampDesc(Road road, Pageable pageable);
    List<TrafficRecord> findByRoadOrderByTimestampDesc(Road road);
    List<TrafficRecord> findTop20ByRoadOrderByTimestampDesc(Road road);
    List<TrafficRecord> findTop20ByOrderByTimestampDesc();
    List<TrafficRecord> findByRoad(Road road);
}
