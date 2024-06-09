# 주식 배당금 정보 조회 서비스 API [2024.01.05 ~ 2024.01.27]

## Development Environment

- Intellij IDEA Ultimate
- Java 17
- Gradle 7.4.1
- Spring Boot 2.7.18

## Tech Stack

- Spring Boot, Spring Security, Spring Data Jpa
- JWT
- H2
- Redis
- Jsoup

## API

1) GET - finance/dividend/{companyName}
- 특정 회사의 메타 정보와 배당금 정보를 반환

2) GET - company/autocomplete
- 입력 prefix로 검색되는 회사명 리스트 중 10개 반환

3) GET - company
- 모든 회사 목록을 반환

4) POST - company
- 새로운 회사 정보 추가

5) DELETE - company/{ticker}
- ticker에 해당하는 회사 정보 삭제

6) POST - auth/signup
- 회원가입

7) POST - auth/signin
- 로그인
