package com.mever.api.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mever.api.domain.email.service.NaverSmsSender;
import com.mever.api.domain.email.service.SendService;
import com.mever.api.domain.member.dto.MemberReq;
import com.mever.api.domain.member.dto.MemberRes;
import com.mever.api.domain.member.entity.Member;
import com.mever.api.domain.member.repository.MemberMapper;
import com.mever.api.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final SendService sendService;
    @Transactional
    public ResponseEntity insMember(MemberReq memberReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
        String category = memberReq.getCategory();
        String email=memberReq.getEmail();
        String test="";
        Member member;
        if (category == null && test.equals(category)) {
            member = memberRepository.findByEmail(email).orElse(null);
        }else {
            member = memberRepository.findByEmailAndCategory(email, category).orElse(null);
        }
        if (member == null) {
            memberReq.setPassword(memberReq.getPhone());
            memberReq.setCategory(memberReq.getCategory());
            if (memberReq.getCategory() != null && !test.equals(memberReq.getCategory())) {
                memberReq.setCategory(memberReq.getCategory());
            }
            memberRepository.save(memberReq.toMemberBuilder());
            if(memberReq.getMessage()!=null&&!memberReq.getMessage().equals("")){
                sendService.sendNaver(memberReq.getPhone(),memberReq.getMessage());
            }
        } else {
            if (memberReq.getPhone() != null && !test.equals(memberReq.getPhone())) {
                member.setPhone(memberReq.getPhone());
            }
            if (memberReq.getAppointment() != null && !test.equals(memberReq.getAppointment())) {
                member.setAppointment(memberReq.getAppointment());
            }
            memberRepository.save(member);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity chkLogin(MemberReq memberReq) {
        String email=memberReq.getEmail();
        String password=memberReq.getPassword();
        boolean member = memberRepository.existsByEmailAndPassword(email,password);
        if(member) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @Transactional
    public Object chkAdmin(MemberReq memberReq) {
        String email=memberReq.getEmail();
        String password=memberReq.getPassword();
        Member member = memberRepository.findByEmailAndPasswordAndAdminYn(email,password,"Y");
        if(member != null) {

            return member;
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public Object memberList(String category,String email) {
//        String email = requestData.get("email");
//        String category = requestData.get("category");
        if(category != null && category != ""){
            List<MemberRes> memberList = memberMapper.getMemberList(category);
            return memberList;
        }
        if(email==null||email.equals("")) {
            return memberRepository.findAll();
        }
        return memberRepository.findByEmail(email).orElse(null);
    }


}
