package com.zerobase.stockservice.service;

import com.zerobase.stockservice.domain.Member;
import com.zerobase.stockservice.dto.Auth;
import com.zerobase.stockservice.exception.AuthException;
import com.zerobase.stockservice.exception.ErrorCode;
import com.zerobase.stockservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    /**
     * authentication이 요구될 경우 UserDetails 조회
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUser(username);
    }

    @Transactional
    public Member register(Auth.SignUp member) {
        if (memberRepository.existsByUsername(member.getUsername())) {
            throw new AuthException(ErrorCode.ALREADY_EXIST_USERNAME);
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member.toEntity());
    }

    public Member authenticate(Auth.SignIn member) {
        Member user = getUser(member.getUsername());
        if (!passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new AuthException(ErrorCode.INVALID_PASSWORD);
        }
        return user;
    }

    private Member getUser(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(ErrorCode.NOT_FOUND_MEMBER));
    }
}
