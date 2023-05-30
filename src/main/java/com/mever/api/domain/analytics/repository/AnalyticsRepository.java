package com.mever.api.domain.analytics.repository;

import com.mever.api.domain.analytics.entity.Analytics;
import com.mever.api.domain.payment.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    public Orders findByPathUrl(String orderId);

}
