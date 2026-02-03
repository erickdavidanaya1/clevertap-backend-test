package com.demo.clevertap_demo.service;

import com.demo.clevertap_demo.dto.CreateEventRequest;
import com.demo.clevertap_demo.model.AppEvent;
import com.demo.clevertap_demo.model.AppUser;
import com.demo.clevertap_demo.repository.AppEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

  @Test
  void shouldCreateEventAndSync() {
    AppEventRepository repo = mock(AppEventRepository.class);
    UserService userService = mock(UserService.class);
    CleverTapSyncService ct = mock(CleverTapSyncService.class);

    AppUser user = new AppUser("user-99", "Erick", "test@test.com");

    when(userService.getByExternalId("user-99")).thenReturn(user);
    when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    EventService service = new EventService(repo, userService, ct, new ObjectMapper());

    CreateEventRequest req = new CreateEventRequest();
    req.userExternalId = "user-99";
    req.name = "purchase";
    req.props = Map.of("amount", 100);

    AppEvent ev = service.create(req);

    assertEquals("purchase", ev.getName());
    verify(repo, times(1)).save(any());
  }
}
