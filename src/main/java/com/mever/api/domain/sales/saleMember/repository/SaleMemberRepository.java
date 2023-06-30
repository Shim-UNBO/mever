package com.mever.api.domain.sales.saleMember.repository;

import com.mever.api.domain.sales.saleMember.entity.SaleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaleMemberRepository extends JpaRepository<SaleMember, Long> {

    public boolean existsByUserIdAndPassword(String userId, String password);
    public Optional<SaleMember> findByUserIdAndPassword(String userId, String password);
    public Optional<SaleMember> findByUserId(String userId);
    boolean existsByUserId(String userId);
}