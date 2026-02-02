package com.demo.clevertap_demo.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class AppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String externalId; // el userId que tú recibes/defines (o email/uuid)

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private Instant createdAt = Instant.now();

  public AppUser() {}

  public AppUser(String externalId, String name, String email) {
    this.externalId = externalId;
    this.name = name;
    this.email = email;
  }

  public Long getId() { return id; }
  public String getExternalId() { return externalId; }
  public String getName() { return name; }
  public String getEmail() { return email; }
  public Instant getCreatedAt() { return createdAt; }

  public void setExternalId(String externalId) { this.externalId = externalId; }
  public void setName(String name) { this.name = name; }
  public void setEmail(String email) { this.email = email; }
}
