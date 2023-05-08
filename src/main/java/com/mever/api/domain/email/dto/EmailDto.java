package com.mever.api.domain.email.dto;

import com.mever.api.domain.email.entity.Mail;
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

    public Mail toMailEntity(Mail mail) {
        mail.setAddress(mail.getAddress());
        mail.setTitle(mail.getTitle());
        mail.setContent(mail.getContent());

        return mail;
    }
}