package com.urbantraffic.platform.repository.crud;

import com.urbantraffic.platform.crud.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrudVehicleTypeRepository extends JpaRepository<VehicleType, Long> {
}