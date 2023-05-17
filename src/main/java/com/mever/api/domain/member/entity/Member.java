package com.mever.api.domain.member.entity;

import com.mever.api.domain.member.dto.MemberRes;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, unique = true)
    private Long seq;

    @Column(nullable = false)
    private String email;
    @Column
    private String phone;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String survey;
    @Column
    private String dcrp;
    @Column
    private Long afterDay;
    @Column
    private String appointment;

    public MemberRes toDto() {
        return MemberRes.builder()
                .email(email)
                .phone(phone)
                .name(name)
                .survey(survey)
                .dcrp(dcrp)
                .afterDay(afterDay)
                .build();
    }
}