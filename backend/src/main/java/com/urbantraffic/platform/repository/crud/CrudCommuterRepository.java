package com.urbantraffic.platform.repository.crud;

import com.urbantraffic.platform.crud.Commuter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrudCommuterRepository extends JpaRepository<Commuter, Long> {
}