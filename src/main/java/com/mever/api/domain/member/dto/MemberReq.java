package com.mever.api.domain.member.dto;

import com.mever.api.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberReq {
    private String email;			// 이메일
    private String phone;			// 휴대폰 번호
    private String password;			// 비밀번호
    private String name;			// 이름
    private String survey;			// 설문
    private String dcrp;		    //메모
    private Long afterDay;		    //메모

    public Member toMemberEntity(Member member) {
        member.setEmail(email);
        member.setPhone(phone);
        member.setName(name);
        member.setSurvey(survey);
        member.setDcrp(dcrp);
        return member;
    }
    public Member toMemberBuilder() {
        return Member.builder()
                .email(email)
                .phone(phone)
                .name(name)
                .survey(survey)
                .dcrp(dcrp)
                .password(phone)
                .afterDay(0L)
                .build();
    }
}