package com.example.flyfishshop.config;

import com.example.flyfishshop.util.JsonResult;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;
import java.util.stream.Collectors;

// 拦截所有rest
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<JsonResult> handle(HandlerMethodValidationException ex) {
        String msg = ex.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(JsonResult.fail(msg));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResult> handle(MethodArgumentNotValidException ex) {
        String msg = ex.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(JsonResult.fail(msg));
    }
}
