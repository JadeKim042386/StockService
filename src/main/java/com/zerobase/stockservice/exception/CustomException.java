package com.zerobase.stockservice.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

    public CustomException(ErrorCode errorCode, Exception causeException) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription() + "|" + causeException.getMessage();
        initCause(causeException);
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
