package com.mever.api.domain.member.controller;

import com.mever.api.domain.member.dto.MemberReq;
import com.mever.api.domain.member.repository.MemberRepository;
import com.mever.api.domain.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원")
@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @PostMapping("/insMember")
    @ApiOperation(value = "회원정보 저장", notes = "회원정보 저장.")
    public ResponseEntity insMember(
            @ApiParam(value = "요청 객체", required = true) @RequestBody MemberReq memberReq) throws Exception {
        try {
            return ResponseEntity.ok(memberService.insMember(memberReq));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/chkLogin")
    @ApiOperation(value = "payment 정보", notes = "payment 정보를 반환합니다.")
    public ResponseEntity chkLogin(
            @ApiParam(value = "요청 객체", required = true) @RequestBody MemberReq memberReq) throws Exception {
        try {
            return ResponseEntity.ok(memberService.chkLogin(memberReq));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/member/list")
    @ApiOperation(value = "회원정보 저장", notes = "회원정보 저장.")
    public ResponseEntity memberList(
            @ApiParam(value = "요청 객체", required = false) @RequestParam(required = false) String email) throws Exception {
        try {
            return ResponseEntity.ok(memberService.memberList(email));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
}
