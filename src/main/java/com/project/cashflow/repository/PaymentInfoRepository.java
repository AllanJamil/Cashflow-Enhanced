package com.project.cashflow.repository;


import com.project.cashflow.domain.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, UUID> {

    List<PaymentInfo> findAllByPaymentId(UUID paymentId);
}
