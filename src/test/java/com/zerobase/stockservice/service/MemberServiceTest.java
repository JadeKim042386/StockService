package com.zerobase.stockservice.service;

import com.zerobase.stockservice.domain.Member;
import com.zerobase.stockservice.dto.Auth;
import com.zerobase.stockservice.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks private MemberService memberService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private MemberRepository memberRepository;

    @DisplayName("회원가입")
    @Test
    void register() {
        //given
        String username = "username";
        String password = "password";
        Set<String> roles = Set.of("ROLE_READ");
        Auth.SignUp signUp = new Auth.SignUp();
        signUp.setUsername(username);
        signUp.setPassword(password);
        signUp.setRoles(roles);
        given(memberRepository.existsByUsername(anyString()))
                .willReturn(false);
        given(passwordEncoder.encode(anyString()))
                .willReturn("aaa");
        Member member = Member.builder()
                .username(username)
                .password(password)
                .roles(roles)
                .build();
        given(memberRepository.save(any())).willReturn(member);
        //when
        Member registeredMember = memberService.register(signUp);
        //then
        assertThat(registeredMember.getUsername()).isEqualTo(username);
        assertThat(registeredMember.getPassword()).isEqualTo(password);
        assertThat(registeredMember.getRoles().contains("ROLE_READ")).isTrue();
    }

    @DisplayName("로그인")
    @Test
    void authenticate() {
        //given
        String username = "username";
        String password = "password";
        Set<String> roles = Set.of("ROLE_READ");
        Auth.SignIn signIn = new Auth.SignIn();
        signIn.setUsername(username);
        signIn.setPassword(password);
        Member member = Member.builder()
                .username(username)
                .password(password)
                .roles(roles)
                .build();
        given(memberRepository.findByUsername(anyString()))
                .willReturn(Optional.of(member));
        given(passwordEncoder.matches(anyString(), anyString()))
                .willReturn(true);
        //when
        Member loginMember = memberService.authenticate(signIn);
        //then
        assertThat(loginMember.getUsername()).isEqualTo(username);
        assertThat(loginMember.getPassword()).isEqualTo(password);
    }

    @DisplayName("사용자 조회")
    @Test
    void loadUserByUsername() {
        //given
        String username = "username";
        String password = "password";
        Set<String> roles = Set.of("ROLE_READ");
        Member member = Member.builder()
                .username(username)
                .password(password)
                .roles(roles)
                .build();
        given(memberRepository.findByUsername(anyString()))
                .willReturn(Optional.of(member));
        //when
        UserDetails userDetails = memberService.loadUserByUsername(username);
        //then
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }
}