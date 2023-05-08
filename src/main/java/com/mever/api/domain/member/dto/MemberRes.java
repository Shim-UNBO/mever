package com.mever.api.domain.member.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRes {
    private String email;			// 이메일
    private String phone;			// 휴대폰 번호
    private String password;		// 비밀번호
    private String name;			// 이름
    private String survey;			// 설문
    private String dcrp;		    //메모
    private Long afterDay;        //가입 후 며칠 지났는지
    private String title;
    private String content;
}