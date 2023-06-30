package com.mever.api.domain.sales.saleMember.entity;


import com.mever.api.domain.sales.saleMember.dto.SaleMemberDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sales_users")
@DynamicInsert
@DynamicUpdate
public class SaleMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
   @Column
    private String userId;
   @Column
    private String password;
   @Column
    private String userNm;
   @Column
    private String userPh;
   @Column
    private LocalDateTime crtDt;
   @Column
    private Long recommender;


    private SaleMemberDto convertToDto(SaleMember saleMember) {
        SaleMemberDto memberDto = new SaleMemberDto();
        memberDto.setId(saleMember.getId());
        memberDto.setUserId(saleMember.getUserId());
        memberDto.setPassword(saleMember.getPassword());
        memberDto.setUserNm(saleMember.getUserNm());
        memberDto.setUserPh(saleMember.getUserPh());
        memberDto.setCrtDt(saleMember.getCrtDt());
        memberDto.setRecommender(saleMember.getRecommender());
        return memberDto;
    }

}