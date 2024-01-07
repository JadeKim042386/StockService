package com.zerobase.stockservice.dto;

import com.zerobase.stockservice.domain.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

public class Auth {
    @Getter
    @Setter
    public static class SignIn {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class SignUp {
        private String username;
        private String password;
        private Set<String> roles;

        public Member toEntity() {
            return Member.builder()
                    .username(username)
                    .password(password)
                    .roles(roles)
                    .build();
        }
    }
}
