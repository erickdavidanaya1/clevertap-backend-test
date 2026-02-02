package com.demo.clevertap_demo.dto;

import java.time.Instant;

public class EventResponse {
  public Long id;
  public String name;
  public Instant timestamp;
  public String payloadJson;

  public EventResponse(Long id, String name, Instant timestamp, String payloadJson) {
    this.id = id;
    this.name = name;
    this.timestamp = timestamp;
    this.payloadJson = payloadJson;
  }
}


