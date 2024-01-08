package com.zerobase.stockservice.exception;

public class AuthException extends CustomException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode, errorCode.getDescription());
    }

    public AuthException(ErrorCode errorCode, Exception causeException) {
        super(errorCode, errorCode.getDescription() + "|" + causeException.getMessage());
        initCause(causeException);
    }
}
