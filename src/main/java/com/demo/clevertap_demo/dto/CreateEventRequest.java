package com.demo.clevertap_demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Map;

public class CreateEventRequest {
  @NotBlank public String userExternalId;
  @NotBlank public String name;

  // si no mandas timestamp, lo ponemos ahora
  public Instant timestamp;

  // propiedades del evento
  @NotNull public Map<String, Object> props;
}

