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
        String email=memberReq.getEmail();
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null) {
            memberReq.setPassword(memberReq.getPhone());
            memberRepository.save(memberReq.toMemberBuilder());
        } else {
            member.setPhone(memberReq.getPhone());
            memberRepository.save(memberReq.toMemberEntity(member));
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


}
