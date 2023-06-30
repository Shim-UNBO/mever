package com.mever.api.domain.sales.saleMember.repository;

import com.mever.api.domain.sales.saleMember.entity.SaleMember;
import com.mever.api.domain.sales.saleMember.entity.SaleRecommender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRecommenderRepository extends JpaRepository<SaleRecommender, Long> {

    public boolean existsByUserIdAndPassword(String userId, String password);
    public Optional<SaleMember> findByUserIdAndPassword(String userId, String password);
    public Optional<SaleMember> findByUserId(String userId);
    public List findByRecommender(Long userId);
    boolean existsByUserId(String userId);
}