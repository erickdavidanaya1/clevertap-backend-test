package com.demo.clevertap_demo.service;

import com.demo.clevertap_demo.client.CleverTapClient;
import com.demo.clevertap_demo.model.AppEvent;
import com.demo.clevertap_demo.model.AppUser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CleverTapSyncService {

  private final CleverTapClient client;
  private final ObjectMapper mapper;

  public CleverTapSyncService(CleverTapClient client, ObjectMapper mapper) {
    this.client = client;
    this.mapper = mapper;
  }

  public ResponseEntity<String> sendUserUpsert(AppUser user) {
    Map<String, Object> profile = new HashMap<>();
    profile.put("Name", user.getName());
    profile.put("Email", user.getEmail());

    Map<String, Object> item = new HashMap<>();
    item.put("identity", user.getExternalId());
    item.put("type", "profile");
    item.put("profile", profile);

    Map<String, Object> body = new HashMap<>();
    body.put("d", List.of(item));

    return client.upload(body);
  }
  
   //envío de evento en CleverTap usando /1/upload.

  public ResponseEntity<String> sendEvent(AppEvent ev, AppUser user) {
    Map<String, Object> eventData = new HashMap<>();
    eventData.put("identity", user.getExternalId());
    eventData.put("type", "event");
    eventData.put("evtName", ev.getName());
    eventData.put("evtData", parseJsonOrEmpty(ev.getPayloadJson()));
    eventData.put("ts", DateTimeFormatter.ISO_INSTANT.format(ev.getTimestamp()));

    Map<String, Object> body = new HashMap<>();
    body.put("d", List.of(eventData));

    return client.upload(body);
  }

  private Map<String, Object> parseJsonOrEmpty(String json) {
    if (json == null || json.isBlank()) return Map.of();
    try {
      return mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
    } catch (Exception e) {
      return Map.of();
    }
  }
}

