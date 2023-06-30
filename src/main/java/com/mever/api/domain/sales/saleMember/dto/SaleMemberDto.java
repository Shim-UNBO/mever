package com.mever.api.domain.sales.saleMember.dto;


import com.mever.api.domain.sales.saleMember.entity.SaleMember;
import com.mever.api.domain.sales.saleMember.entity.SaleRecommender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleMemberDto {
    private Long id;
    private String userId;
    private String password;
    private String userNm;
    private String userPh;
    private LocalDateTime crtDt;
    private Long recommender;
    private String recommenderId;
    private Long type;

    public static SaleMember toMemberEntity(SaleMemberDto saleMemberDto) {
        SaleMember member = new SaleMember();
        member.setUserId(saleMemberDto.getUserId());
        member.setPassword(saleMemberDto.getPassword());
        member.setUserNm(saleMemberDto.getUserNm());
        member.setUserPh(saleMemberDto.getUserPh());
        member.setRecommender(saleMemberDto.getRecommender());
        return member;
    }
    public static SaleRecommender toRecommenderEntity(SaleMemberDto saleMemberDto) {
        SaleRecommender saleRecommender = new SaleRecommender();
        saleRecommender.setUserId(saleMemberDto.getUserId());
        saleRecommender.setType(saleMemberDto.getType());
        saleRecommender.setUserNm(saleMemberDto.getUserNm());
        saleRecommender.setUserPh(saleMemberDto.getUserPh());
        saleRecommender.setRecommender(saleMemberDto.getRecommender());
        return saleRecommender;
    }
}