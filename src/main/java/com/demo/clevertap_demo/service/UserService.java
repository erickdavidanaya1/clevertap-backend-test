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

  public UserService(AppUserRepository repo) {
    this.repo = repo;
  }

  public AppUser upsert(CreateUserRequest req) {
    String externalId = req.externalId.trim();

    return repo.findByExternalId(externalId)
        .map(u -> {
          u.setName(req.name);
          u.setEmail(req.email);
          return repo.save(u);
        })
        .orElseGet(() -> repo.save(new AppUser(externalId, req.name, req.email)));
  }

  public AppUser getByExternalId(String externalId) {
    return repo.findByExternalId(externalId)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user not found"));
  }
}
