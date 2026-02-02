package com.demo.clevertap_demo.service;

import com.demo.clevertap_demo.dto.CreateEventRequest;
import com.demo.clevertap_demo.model.AppEvent;
import com.demo.clevertap_demo.model.AppUser;
import com.demo.clevertap_demo.repository.AppEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EventService {

  private final AppEventRepository eventsRepo;
  private final UserService userService;
  private final ObjectMapper mapper = new ObjectMapper();

  public EventService(AppEventRepository eventsRepo, UserService userService) {
    this.eventsRepo = eventsRepo;
    this.userService = userService;
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

    AppEvent ev = new AppEvent(user, req.name, ts, payloadJson);
    return eventsRepo.save(ev);
  }

  public List<AppEvent> listByUserId(Long userId) {
    return eventsRepo.findByUser_IdOrderByTimestampDesc(userId);
  }
}
