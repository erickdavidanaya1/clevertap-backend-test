package com.demo.clevertap_demo.service;

import com.demo.clevertap_demo.client.CleverTapClient;
import com.demo.clevertap_demo.model.AppEvent;
import com.demo.clevertap_demo.model.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CleverTapSyncService {

  private final CleverTapClient client;

  public CleverTapSyncService(CleverTapClient client) {
    this.client = client;
  }

  public ResponseEntity<String> sendUserUpsert(AppUser user) {
    Map<String, Object> profile = new HashMap<>();
    profile.put("identity", user.getExternalId());
    profile.put("name", user.getName());
    profile.put("email", user.getEmail());

    Map<String, Object> body = new HashMap<>();
    body.put("d", List.of(Map.of("type", "profile", "profileData", profile)));

    return client.upload(body);
  }

  public ResponseEntity<String> sendEvent(AppEvent ev, String userExternalId) {
    Map<String, Object> eventData = new HashMap<>();
    eventData.put("identity", userExternalId);
    eventData.put("type", "event");
    eventData.put("evtName", ev.getName());
    eventData.put("evtData", parseJsonOrEmpty(ev.getPayloadJson()));

    // timestamp ISO
    eventData.put("ts", DateTimeFormatter.ISO_INSTANT.format(ev.getTimestamp()));

    Map<String, Object> body = new HashMap<>();
    body.put("d", List.of(eventData));

    return client.upload(body);
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> parseJsonOrEmpty(String json) {
    try {
      // parse rápido sin meter dependencia extra
      return new com.fasterxml.jackson.databind.ObjectMapper().readValue(json, Map.class);
    } catch (Exception e) {
      return Map.of();
    }
  }
}

