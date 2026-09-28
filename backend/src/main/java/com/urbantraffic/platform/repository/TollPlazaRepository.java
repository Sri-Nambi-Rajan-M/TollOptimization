package com.urbantraffic.platform.repository;

import com.urbantraffic.platform.entity.TollPlaza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TollPlazaRepository extends JpaRepository<TollPlaza, Long> {
    List<TollPlaza> findByRoadId(Long roadId);
}
