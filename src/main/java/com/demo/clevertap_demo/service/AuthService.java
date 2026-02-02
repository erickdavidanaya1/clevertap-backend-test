package com.demo.clevertap_demo.service;

import com.demo.clevertap_demo.dto.AuthResponse;
import com.demo.clevertap_demo.dto.LoginRequest;
import com.demo.clevertap_demo.dto.RegisterRequest;
import com.demo.clevertap_demo.exception.ApiException;
import com.demo.clevertap_demo.model.AuthUser;
import com.demo.clevertap_demo.repository.AuthUserRepository;
import com.demo.clevertap_demo.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final AuthUserRepository repo;
  private final PasswordEncoder encoder;
  private final JwtService jwt;

  public AuthService(AuthUserRepository repo, PasswordEncoder encoder, JwtService jwt) {
    this.repo = repo;
    this.encoder = encoder;
    this.jwt = jwt;
  }

  public AuthResponse register(RegisterRequest req) {
    String email = req.email.trim().toLowerCase();

    if (repo.existsByEmail(email)) {
      throw new ApiException(HttpStatus.CONFLICT, "email already registered");
    }

    AuthUser user = new AuthUser(email, encoder.encode(req.password));
    repo.save(user);

    String token = jwt.generateToken(email);
    return new AuthResponse(token, jwt.getExpirationSeconds());
  }

  public AuthResponse login(LoginRequest req) {
    String email = req.email.trim().toLowerCase();

    AuthUser user = repo.findByEmail(email)
        .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "invalid credentials"));

    if (!encoder.matches(req.password, user.getPasswordHash())) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "invalid credentials");
    }

    String token = jwt.generateToken(email);
    return new AuthResponse(token, jwt.getExpirationSeconds());
  }
}
