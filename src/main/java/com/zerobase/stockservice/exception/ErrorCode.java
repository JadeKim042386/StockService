package com.zerobase.stockservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR_CODE("내부 서버 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    NOT_FOUND_TABLE("테이블 element를 찾을 수 없습니다."),
    NOT_FOUND_COMPANY("존재하지 않는 기업입니다."),
    NOT_FOUND_MEMBER("존재하지 않는 유저입니다."),
    ALREADY_EXIST_TICKER("이미 존재하는 Ticker 입니다."),
    ALREADY_EXIST_USERNAME("이미 존재하는 아이디 입니다."),
    FAILED_SCRAP("스크래핑에 실패했습니다."),
    EMPTY_TICKER("Ticker가 입력되지 않았습니다."),
    NOT_EXIST_CACHE("캐시가 존재하지 않습니다."),
    INTERRUPTED_THREAD("스케줄러로 배당금 정보를 스크래핑하던 중 스레드 인터럽트가 발생했습니다."),
    INVALID_ENUM("잘못된 enum 값입니다."),
    FAILED_GET_DOCUMENT("스크래핑을 위한 document를 얻는데 실패했습니다."),
    INVALID_PASSWORD("잘못된 비밀번호입니다.")
    ;
    private final String description;
}
