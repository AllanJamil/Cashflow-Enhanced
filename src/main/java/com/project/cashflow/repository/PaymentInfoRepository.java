package com.project.cashflow.repository;


import com.project.cashflow.domain.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentDetails, UUID> {
}
