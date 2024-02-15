package com.okky.restserver.security.handler;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.okky.restserver.dto.ErrorDto;
import com.okky.restserver.dto.ResponseCode;
import com.okky.restserver.security.exception.DuplicateUserException;
import com.okky.restserver.security.exception.NotFoundMemberException;

/**
 * 
 * @author hyunj
 *
 */

@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(DuplicateUserException.class)
    protected ErrorDto conflict() {
        return ErrorDto.builder()
        		.status(ResponseCode.E0006.getStatus().value())
        		.code(ResponseCode.E0006.name())
        		.message(ResponseCode.E0006.getMessage()).build();
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler({NotFoundMemberException.class, AccessDeniedException.class })
    protected ErrorDto forbidden() {
        return ErrorDto.builder()
        		.status(ResponseCode.E0000.getStatus().value())
        		.code(ResponseCode.E0000.name())
        		.message(ResponseCode.E0000.getMessage()).build();
    }
    
}
