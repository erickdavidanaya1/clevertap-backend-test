// src/main/java/com/demo/clevertap_demo/service/UserService.java
package com.demo.clevertap_demo.service;

import com.demo.clevertap_demo.dto.CreateUserRequest;
import com.demo.clevertap_demo.exception.ApiException;
import com.demo.clevertap_demo.model.AppUser;
import com.demo.clevertap_demo.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

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

    // Sync a CleverTap (no tumba la API si falla)
    try {
      var resp = cleverTapSyncService.sendUserUpsert(saved);
      System.out.println("[CleverTap] profile sync status=" + resp.getStatusCode());
      System.out.println("[CleverTap] profile sync body=" + resp.getBody());
    } catch (Exception e) {
      System.out.println("[CleverTap] profile sync failed: " + e.getMessage());
    }

    return saved;
  }

  public AppUser getByExternalId(String externalId) {
    return repo.findByExternalId(externalId)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user not found"));
  }
}


