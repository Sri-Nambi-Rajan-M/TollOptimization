package com.urbantraffic.platform.repository;

import com.urbantraffic.platform.entity.TollPlaza;
import com.urbantraffic.platform.entity.TollRate;
import com.urbantraffic.platform.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TollRateRepository extends JpaRepository<TollRate, Long> {
    List<TollRate> findByTollPlaza(TollPlaza tollPlaza);
    List<TollRate> findByTollPlazaAndVehicleType(TollPlaza tollPlaza, VehicleType vehicleType);
}
