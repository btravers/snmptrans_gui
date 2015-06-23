package com.zenika.snmptrans.gui.web;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenika.snmptrans.gui.exception.SnmptransGuiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class SnmptransGuiControllerErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(SnmptransGuiControllerErrorHandler.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ObjectMapper mapper;

    @ExceptionHandler(value = { MethodArgumentNotValidException.class, SnmptransGuiException.class })
    public ResponseEntity<Object> handleExecption(Exception exception, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (exception instanceof MethodArgumentNotValidException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            try {
                return new ResponseEntity<Object>(
                        this.mapper.writeValueAsString(this.processValidationError(
                                (MethodArgumentNotValidException) exception)
                                .getFieldErrors()), headers, status);
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
            }
        } else if (exception instanceof SnmptransGuiException) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(status);
        }

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(status);
    }

    private ValidationError processValidationError(
            MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return processFieldErrors(fieldErrors);
    }

    private ValidationError processFieldErrors(List<FieldError> fieldErrors) {
        ValidationError validationError = new ValidationError();

        for (FieldError fieldError : fieldErrors) {
            String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            validationError.addFieldError(fieldError.getField(), localizedErrorMessage);
        }

        return validationError;
    }

    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError,
                currentLocale);

        // If the message was not found, return the most accurate field error
        // code instead.
        // You can remove this check if you prefer to get the default error
        // message.
        if (localizedErrorMessage.equals(fieldError.getDefaultMessage())) {
            String[] fieldErrorCodes = fieldError.getCodes();
            localizedErrorMessage = fieldErrorCodes[0];
        }

        return localizedErrorMessage;
    }

}
