package com.demo.clevertap_demo.service;

import com.demo.clevertap_demo.dto.CreateUserRequest;
import com.demo.clevertap_demo.model.AppUser;
import com.demo.clevertap_demo.repository.AppUserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @Test
  void shouldCreateNewUser() {
    AppUserRepository repo = mock(AppUserRepository.class);
    CleverTapSyncService ct = mock(CleverTapSyncService.class);

    when(repo.findByExternalId("user-1")).thenReturn(Optional.empty());
    when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

    UserService service = new UserService(repo, ct);

    CreateUserRequest req = new CreateUserRequest();
    req.externalId = "user-1";
    req.name = "Danilo";
    req.email = "test@test.com";

    AppUser saved = service.upsert(req);

    assertEquals("user-1", saved.getExternalId());
    verify(repo, times(1)).save(any());
  }
}
