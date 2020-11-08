package com.project.cashflow.repository;


import com.project.cashflow.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByIdAndUser_Email(UUID id, String user_email);

    List<Member> findAllByUser_Email(String userEmail);
}
