package com.demo.clevertap_demo.service;

import com.demo.clevertap_demo.dto.CreateEventRequest;
import com.demo.clevertap_demo.model.AppEvent;
import com.demo.clevertap_demo.model.AppUser;
import com.demo.clevertap_demo.repository.AppEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EventService {

  private static final Logger log = LoggerFactory.getLogger(EventService.class);

  private final AppEventRepository eventsRepo;
  private final UserService userService;
  private final CleverTapSyncService cleverTapSyncService;
  private final ObjectMapper mapper;

  public EventService(
      AppEventRepository eventsRepo,
      UserService userService,
      CleverTapSyncService cleverTapSyncService,
      ObjectMapper mapper
  ) {
    this.eventsRepo = eventsRepo;
    this.userService = userService;
    this.cleverTapSyncService = cleverTapSyncService;
    this.mapper = mapper;
  }

  public AppEvent create(CreateEventRequest req) {
    AppUser user = userService.getByExternalId(req.userExternalId);

    Instant ts = (req.timestamp != null) ? req.timestamp : Instant.now();

    String payloadJson;
    try {
      payloadJson = mapper.writeValueAsString(req.props);
    } catch (Exception e) {
      payloadJson = "{}";
    }

    AppEvent saved = eventsRepo.save(new AppEvent(user, req.name, ts, payloadJson));

    // Sync a CleverTap (no debería tumbar la API si falla)
    try {
      var resp = cleverTapSyncService.sendEvent(saved, user);
      log.info("[CleverTap] event sync status={}", resp.getStatusCode());
      log.debug("[CleverTap] event sync body={}", resp.getBody());
    } catch (Exception e) {
      log.warn("[CleverTap] event sync failed: {}", e.getMessage());
    }

    return saved;
  }

  public List<AppEvent> listByUserId(Long userId) {
    return eventsRepo.findByUser_IdOrderByTimestampDesc(userId);
  }
}



