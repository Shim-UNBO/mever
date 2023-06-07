package com.mever.api.domain.member.service;

import com.mever.api.domain.member.dto.MemberReq;
import com.mever.api.domain.member.entity.Member;
import com.mever.api.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    @Transactional
    public ResponseEntity insMember(MemberReq memberReq) {
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
        } else {
            if (memberReq.getPhone() != null && !test.equals(memberReq.getPhone())) {
                member.setPhone(memberReq.getPhone());
//                System.out.println("123");
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
    public Object memberList(String email) {
        if(email==null||email.equals("")) {
            return memberRepository.findAll();
        }
        return memberRepository.findByEmail(email).orElse(null);
    }


}
