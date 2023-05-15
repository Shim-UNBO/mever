package com.mever.api.domain.email.dto;

import com.mever.api.domain.email.entity.Mail;
import com.mever.api.domain.email.entity.SendHistory;
import com.mever.api.domain.member.entity.Member;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsDto {
    private String userkey;
    private String userid;
    private String callback;
    private String msg;
    private String phone;
    private String email;
    private String type;
    //문자대표 api response
    private String result_code;
    private String result_msg;
    private String return_var;
    private String total_count;
    private String succ_count;
    private String fail_count;
    private String money;
    private String mseq;

    public SendHistory toSendBuilder() {
        return SendHistory.builder()
                .email(email)
                .phone(phone)
                .content(msg)
                .type("sms")
                .build();
    }
}