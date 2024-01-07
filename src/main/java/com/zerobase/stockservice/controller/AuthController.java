package com.zerobase.stockservice.controller;

import com.zerobase.stockservice.domain.Member;
import com.zerobase.stockservice.dto.Auth;
import com.zerobase.stockservice.security.TokenProvider;
import com.zerobase.stockservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<Member> signup(@RequestBody Auth.SignUp request) {
        return ResponseEntity.ok(memberService.register(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody Auth.SignIn request) {
        Member member = memberService.authenticate(request);
        return ResponseEntity.ok(tokenProvider.generateToken(member.getUsername(), member.getRoles()));
    }
}
