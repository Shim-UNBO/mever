package com.mever.api.domain.email.service;

import com.mever.api.domain.email.dto.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String FROM_ADDRESS;
    private final JavaMailSender emailSender;

    @Transactional
    public ResponseEntity sendMultipleMessage(EmailDto mailDto, MultipartFile file) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //메일 제목 설정
        helper.setSubject(mailDto.getTitle());

        //참조자 설정
        //helper.setCc(mailDto.getCcAddress());

        helper.setText(mailDto.getContent(), false);

        helper.setFrom(FROM_ADDRESS);

        //첨부 파일 설정
       /* String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        helper.addAttachment(MimeUtility.encodeText(fileName, "UTF-8", "B"), new ByteArrayResource(IOUtils.toByteArray(file.getInputStream())));
*/
      //  수신자 개별 전송
        /*for(String s : mailDto.getAddress()) {
        	helper.setTo(s);
        	emailSender.send(message);
        }*/
        //수신자 한번에 전송
        helper.setTo(mailDto.getAddress());
        emailSender.send(message);
        log.info("mail multiple send complete.");

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Transactional
    public void sendMessage(EmailDto mailDto) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //메일 제목 설정
        helper.setSubject(mailDto.getTitle());

        //참조자 설정
        //helper.setCc(mailDto.getCcAddress());

        helper.setText(mailDto.getContent(), false);

        helper.setFrom(FROM_ADDRESS);

        //첨부 파일 설정
       /* String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        helper.addAttachment(MimeUtility.encodeText(fileName, "UTF-8", "B"), new ByteArrayResource(IOUtils.toByteArray(file.getInputStream())));
*/
        //  수신자 개별 전송
        /*for(String s : mailDto.getAddress()) {
        	helper.setTo(s);
        	emailSender.send(message);
        }*/
        //수신자 한번에 전송
        helper.setTo(mailDto.getAddress());
        emailSender.send(message);

    }
}
