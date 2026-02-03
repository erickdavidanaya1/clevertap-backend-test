package com.demo.clevertap_demo.service;

import com.demo.clevertap_demo.dto.CreateUserRequest;
import com.demo.clevertap_demo.exception.ApiException;
import com.demo.clevertap_demo.model.AppUser;
import com.demo.clevertap_demo.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private static final Logger log = LoggerFactory.getLogger(UserService.class);

  private final AppUserRepository repo;
  private final CleverTapSyncService cleverTapSyncService;

  public UserService(AppUserRepository repo, CleverTapSyncService cleverTapSyncService) {
    this.repo = repo;
    this.cleverTapSyncService = cleverTapSyncService;
  }

  public AppUser upsert(CreateUserRequest req) {
    String externalId = req.externalId.trim();

    AppUser saved = repo.findByExternalId(externalId)
        .map(u -> {
          u.setName(req.name);
          u.setEmail(req.email);
          return repo.save(u);
        })
        .orElseGet(() -> repo.save(new AppUser(externalId, req.name, req.email)));

    // Sync a CleverTap (no debería tumbar la API si falla)
    try {
      var resp = cleverTapSyncService.sendUserUpsert(saved);
      log.info("[CleverTap] profile sync status={}", resp.getStatusCode());
      log.debug("[CleverTap] profile sync body={}", resp.getBody());
    } catch (Exception e) {
      log.warn("[CleverTap] profile sync failed: {}", e.getMessage());
    }

    return saved;
  }

  public AppUser getByExternalId(String externalId) {
    return repo.findByExternalId(externalId)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user not found"));
  }
}




