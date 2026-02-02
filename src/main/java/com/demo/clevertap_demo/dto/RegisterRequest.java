package com.demo.clevertap_demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
  @Email @NotBlank
  public String email;

  @NotBlank
  @Size(min = 8, message = "password must be at least 8 chars")
  public String password;
}

