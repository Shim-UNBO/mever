package com.mever.api.domain.mainAdmin.repository;

import com.mever.api.domain.mainAdmin.entity.MainTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainTitleRepository extends JpaRepository<MainTitle, Long> {
    public MainTitle findByCategory(String category);
}
