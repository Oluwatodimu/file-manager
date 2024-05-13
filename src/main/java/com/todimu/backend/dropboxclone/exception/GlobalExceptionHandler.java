package com.todimu.backend.dropboxclone.exception;

import com.todimu.backend.dropboxclone.data.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UsernameNotFoundException.class, TokenValidationFailedException.class, AuthenticationException.class})
    public ResponseEntity<BaseResponse> unauthorizedExceptions(Exception exception) {
        log.error(exception.getMessage(), exception.getLocalizedMessage());
        return new ResponseEntity<>(new BaseResponse(null, exception.getMessage(), true), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadCredentialsException.class, EmailAlreadyExistsException.class, FolderAlreadyExistsException.class})
    public ResponseEntity<BaseResponse> badRequestExceptions(Exception exception) {
        log.error(exception.getMessage(), exception.getLocalizedMessage());
        return new ResponseEntity<>(new BaseResponse(null, exception.getMessage(), true), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<BaseResponse> notFoundExceptions(Exception exception) {
        log.error(exception.getMessage(), exception.getLocalizedMessage());
        return new ResponseEntity<>(new BaseResponse(null, exception.getMessage(), true), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(new BaseResponse(null, validationErrors.toString(), true), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<BaseResponse> handleAllExceptions(Exception ex) {
        log.error(ex.getMessage());
        log.error(ex.getMessage(), ex.getLocalizedMessage());
        return new ResponseEntity<>(new BaseResponse(null, (ex.getMessage() != null) ? ex.getMessage() : "Oops something went wrong !!!", true), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
