package com.demo.clevertap_demo.controller;

import com.demo.clevertap_demo.dto.CreateUserRequest;
import com.demo.clevertap_demo.dto.EventResponse;
import com.demo.clevertap_demo.model.AppUser;
import com.demo.clevertap_demo.service.EventService;
import com.demo.clevertap_demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

  private final UserService userService;
  private final EventService eventService;

  public UserController(UserService userService, EventService eventService) {
    this.userService = userService;
    this.eventService = eventService;
  }

  @PostMapping("/users")
  public AppUser upsert(@Valid @RequestBody CreateUserRequest req) {
    return userService.upsert(req);
  }

  @GetMapping("/users/{userId}/events")
  public List<EventResponse> listEvents(@PathVariable Long userId) {
    return eventService.listByUserId(userId).stream()
        .map(e -> new EventResponse(
            e.getId(),
            e.getName(),
            e.getTimestamp(),
            e.getPayloadJson()
        ))
        .toList();
  }
}


