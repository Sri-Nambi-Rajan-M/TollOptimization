package com.urbantraffic.platform.repository.crud;

import com.urbantraffic.platform.crud.TollTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrudTollTransactionRepository extends JpaRepository<TollTransaction, Long> {
}