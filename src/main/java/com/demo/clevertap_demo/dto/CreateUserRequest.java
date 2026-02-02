package com.demo.clevertap_demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {
  @NotBlank public String externalId;
  @NotBlank public String name;
  @Email @NotBlank public String email;
}
