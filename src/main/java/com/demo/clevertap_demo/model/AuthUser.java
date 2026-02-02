package com.demo.clevertap_demo.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "auth_users",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_auth_users_email", columnNames = "email")
    }
)
public class AuthUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false, name = "password_hash")
  private String passwordHash;

  @Column(nullable = false, name = "created_at")
  private Instant createdAt = Instant.now();

  public AuthUser() {}

  public AuthUser(String email, String passwordHash) {
    this.email = email;
    this.passwordHash = passwordHash;
  }

  public Long getId() { return id; }
  public String getEmail() { return email; }
  public String getPasswordHash() { return passwordHash; }
  public Instant getCreatedAt() { return createdAt; }

  public void setEmail(String email) { this.email = email; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}

