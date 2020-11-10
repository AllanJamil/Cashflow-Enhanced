package com.project.cashflow.repository;


import com.project.cashflow.domain.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, UUID> {

    List<Bill> findAllByMemberId(UUID memberId);

    Optional<Bill> findByIdAndMember_User_Email(UUID id, String userEmail);

    Optional<Bill> findByIdAndMember_Id(UUID id, UUID memberId);
}
