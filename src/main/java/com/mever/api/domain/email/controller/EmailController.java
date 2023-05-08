package com.mever.api.domain.email.controller;

import com.mever.api.domain.email.dto.EmailDto;
import com.mever.api.domain.email.service.EmailService;
import com.mever.api.domain.schedule.ScheduledTasks;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "mail")
@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private ScheduledTasks scheduledTasks;

    @GetMapping("/textMail")
    public String mailSend() {
        return "textMail";
    }
    @PostMapping("/mail/send")
    @Operation(summary  = "메일 전송", description = "메일 전송합니다.")
    public ResponseEntity sendMail(
            @ApiParam(value = "요청 객체", required = true) @RequestBody EmailDto emailDto, MultipartFile file) throws Exception {
        try {
            return ResponseEntity.ok(emailService.sendMultipleMessage(emailDto, file));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/automail")
    public void autoMail() throws Exception {
        try {
            scheduledTasks.cronMailSend();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
}
