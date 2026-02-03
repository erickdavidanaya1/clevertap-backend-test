package com.demo.clevertap_demo.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

  @Test
  void shouldGenerateAndValidateToken() {
    String secret = "this_is_a_super_secret_key_32_chars!";
    long expiration = 3600000; // 1 hora

    JwtService jwtService = new JwtService(secret, expiration);

    String token = jwtService.generateToken("test@test.com");

    assertNotNull(token);
    assertTrue(jwtService.isTokenValid(token));
    assertEquals("test@test.com", jwtService.extractSubject(token));
  }

  @Test
  void shouldFailWithInvalidToken() {
    String secret = "this_is_a_super_secret_key_32_chars!";
    JwtService jwtService = new JwtService(secret, 3600000);

    assertFalse(jwtService.isTokenValid("esto_no_es_un_token"));
  }

  @Test
  void shouldReturnExpirationInSeconds() {
    JwtService jwtService = new JwtService(
        "this_is_a_super_secret_key_32_chars!",
        60000
    );

    assertEquals(60, jwtService.getExpirationSeconds());
  }
}

