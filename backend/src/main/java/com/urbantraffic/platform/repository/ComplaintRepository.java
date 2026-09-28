package com.urbantraffic.platform.repository;

import com.urbantraffic.platform.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
}