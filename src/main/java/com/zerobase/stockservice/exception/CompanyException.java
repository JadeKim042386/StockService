package com.zerobase.stockservice.exception;

public class CompanyException extends CustomException {
    public CompanyException(ErrorCode errorCode) {
        super(errorCode, errorCode.getDescription());
    }

    public CompanyException(ErrorCode errorCode, Exception causeException) {
        super(errorCode, errorCode.getDescription() + "|" + causeException.getMessage());
        initCause(causeException);
    }
}
