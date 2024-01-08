package com.zerobase.stockservice.exception;

import com.zerobase.stockservice.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.zerobase.stockservice.exception.ErrorCode.INTERNAL_SERVER_ERROR_CODE;
import static com.zerobase.stockservice.exception.ErrorCode.INVALID_REQUEST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<String> handleMethodArgsException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException is occurred.", e);
        return Response.error(INVALID_REQUEST.getDescription());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Response<String> handleDataViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException is occurred.", e);
        return Response.error(INVALID_REQUEST.getDescription());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Response<String> handleAllException(Exception e) {
        log.error("Exception is occurred.", e);
        return Response.error(INTERNAL_SERVER_ERROR_CODE.getDescription());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {CompanyException.class, AuthException.class, ScraperException.class})
    public Response<String> handleDiaryException(CompanyException e) {
        return Response.error(e.getErrorMessage());
    }
}
