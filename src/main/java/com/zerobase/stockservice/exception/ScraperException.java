package com.zerobase.stockservice.exception;

public class ScraperException extends CustomException {

    public ScraperException(ErrorCode errorCode) {
        super(errorCode, errorCode.getDescription());
    }

    public ScraperException(ErrorCode errorCode, Exception causeException) {
        super(errorCode, errorCode.getDescription() + "|" + causeException.getMessage());
        initCause(causeException);
    }
}
