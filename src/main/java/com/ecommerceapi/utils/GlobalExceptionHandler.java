//package com.ecommerceapi.utils;
//
//import jakarta.validation.ConstraintViolationException;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@ControllerAdvice
//public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//
//        List<String> errors = ex.getBindingResult()
//                .getFieldErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
//        body.put("Mensagem de erro", errors);
//        body.put("Campo", ex.getFieldError().getField());
//        return new ResponseEntity<>(body,headers,status);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("errors", ex.getMessage());
//        return new ResponseEntity<>(body, headers, status);
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    ResponseEntity<String> handleConstraintValidationException(ConstraintViolationException ex){
//        return new ResponseEntity<>("Error:"+ ex.getMessage(),HttpStatus.BAD_REQUEST);
//    }
//}
