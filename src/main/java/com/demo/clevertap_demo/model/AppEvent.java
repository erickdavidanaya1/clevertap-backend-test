package com.demo.clevertap_demo.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "events")
public class AppEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private AppUser user;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Instant timestamp = Instant.now();

  @Column(columnDefinition = "TEXT")
  private String payloadJson; // guardar props como JSON string simple

  public AppEvent() {}

  public AppEvent(AppUser user, String name, Instant timestamp, String payloadJson) {
    this.user = user;
    this.name = name;
    this.timestamp = timestamp;
    this.payloadJson = payloadJson;
  }

  public Long getId() { return id; }
  public AppUser getUser() { return user; }
  public String getName() { return name; }
  public Instant getTimestamp() { return timestamp; }
  public String getPayloadJson() { return payloadJson; }

  public void setUser(AppUser user) { this.user = user; }
  public void setName(String name) { this.name = name; }
  public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
  public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
}

