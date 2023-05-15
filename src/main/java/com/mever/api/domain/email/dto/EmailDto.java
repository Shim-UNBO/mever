package com.mever.api.domain.email.dto;

import com.mever.api.domain.email.entity.Mail;
import com.mever.api.domain.email.entity.SendHistory;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    private String from;
    private String address;
    private String[] ccAddress;
    private String title;
    private String content;
    private String phone;

    public SendHistory toMailBuilder() {
        return SendHistory.builder()
                .email(address)
                .phone(phone)
                .title(title)
                .content(content)
                .type("mail")
                .build();
    }
}