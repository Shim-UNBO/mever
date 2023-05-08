package com.mever.api.domain.payment.repository;

import com.mever.api.domain.payment.dto.PaymentResHandleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    List<PaymentResHandleDto> getPayList() ;
}
