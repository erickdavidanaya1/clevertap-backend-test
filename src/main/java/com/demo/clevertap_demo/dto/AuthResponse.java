package com.demo.clevertap_demo.dto;

public class AuthResponse {
  public String token;
  public long expiresInSeconds;

  public AuthResponse(String token, long expiresInSeconds) {
    this.token = token;
    this.expiresInSeconds = expiresInSeconds;
  }
}
