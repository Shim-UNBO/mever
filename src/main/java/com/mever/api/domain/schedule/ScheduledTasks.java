package com.mever.api.domain.schedule;

import com.mever.api.domain.email.dto.EmailDto;
import com.mever.api.domain.email.service.EmailService;
import com.mever.api.domain.member.dto.MemberRes;
import com.mever.api.domain.member.entity.Member;
import com.mever.api.domain.member.repository.MemberMapper;
import com.mever.api.domain.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTasks {

    @Autowired
    private EmailService emailService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberMapper memberMapper;


   // @Scheduled(cron = "0 26 18 * * ?")
    public void cronMailSend() throws MessagingException {
        List<MemberRes> memberRes =memberMapper.getMemberList();

        for(int i =0; i<memberRes.size();i++){
            EmailDto mailDto=EmailDto.builder()
                .address(memberRes.get(i).getEmail())
                .title(String.valueOf(memberRes.get(i).getTitle()))
                .content(String.valueOf(memberRes.get(i).getContent()))
                .build();
            emailService.sendMessage(mailDto);
            Member member = memberRepository.findByEmail(memberRes.get(i).getEmail()).orElse(null);
            member.setAfterDay(Long.valueOf(memberRes.get(i).getAfterDay())+1);
            memberRepository.save(member);
        }
    }
}
