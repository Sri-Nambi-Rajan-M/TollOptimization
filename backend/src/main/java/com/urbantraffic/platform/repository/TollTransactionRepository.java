package com.urbantraffic.platform.repository;

import com.urbantraffic.platform.entity.PaymentStatus;
import com.urbantraffic.platform.entity.TollTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TollTransactionRepository extends JpaRepository<TollTransaction, Long> {
    List<TollTransaction> findAllByOrderByCreatedAtDesc();

    List<TollTransaction> findByCustomerEmailOrderByCreatedAtDesc(String customerEmail);

    long countByStatus(PaymentStatus status);

    @Query("select coalesce(sum(transaction.amount), 0) from TollTransaction transaction where transaction.status = :status")
    double sumAmountByStatus(@Param("status") PaymentStatus status);
}