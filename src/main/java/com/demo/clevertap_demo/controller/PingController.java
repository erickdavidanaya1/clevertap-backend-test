package com.demo.clevertap_demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PingController {

  @GetMapping("/ping")
  public Map<String, Object> ping(Authentication auth) {
    return Map.of(
        "ok", true,
        "principal", auth != null ? auth.getName() : null
    );
  }
}
