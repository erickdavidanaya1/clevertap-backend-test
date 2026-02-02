package com.demo.clevertap_demo.controller;

import com.demo.clevertap_demo.dto.CreateEventRequest;
import com.demo.clevertap_demo.model.AppEvent;
import com.demo.clevertap_demo.service.EventService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class EventController {

  private final EventService eventService;

  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @PostMapping("/events")
  public AppEvent create(@Valid @RequestBody CreateEventRequest req) {
    return eventService.create(req);
  }
}

