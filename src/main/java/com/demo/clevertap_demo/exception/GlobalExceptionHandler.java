package com.demo.clevertap_demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Map<String, Object>> handle(ApiException ex) {
    return ResponseEntity
        .status(ex.getStatus())
        .body(Map.of(
            "error", ex.getMessage(),
            "status", ex.getStatus().value()
        ));
  }
}

