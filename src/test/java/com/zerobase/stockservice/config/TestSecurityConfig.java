package com.zerobase.stockservice.config;

import com.zerobase.stockservice.security.SecurityConfig;
import com.zerobase.stockservice.security.TokenProvider;
import com.zerobase.stockservice.service.MemberService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@Import(SecurityConfig.class)
public class TestSecurityConfig {
    @MockBean private MemberService memberService;
    @MockBean private TokenProvider tokenProvider;
}
