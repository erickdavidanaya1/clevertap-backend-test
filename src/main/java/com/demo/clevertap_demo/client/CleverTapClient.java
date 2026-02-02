package com.demo.clevertap_demo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
public class CleverTapClient {

  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${clevertap.accountId}")
  private String accountId;

  @Value("${clevertap.passcode}")
  private String passcode;

  @Value("${clevertap.region:in1}")
  private String region;

  private HttpHeaders headers() {
    HttpHeaders h = new HttpHeaders();
    h.setContentType(MediaType.APPLICATION_JSON);

    String basic = accountId + ":" + passcode;
    String encoded = Base64.getEncoder().encodeToString(basic.getBytes(StandardCharsets.UTF_8));
    h.set("Authorization", "Basic " + encoded);

    return h;
  }

  private String baseUrl() {
    // API endpoint típico por región
    return "https://" + region + ".api.clevertap.com/1";
  }

  public ResponseEntity<String> upload(Map<String, Object> payload) {
    String url = baseUrl() + "/upload";
    HttpEntity<Map<String, Object>> req = new HttpEntity<>(payload, headers());
    return restTemplate.exchange(url, HttpMethod.POST, req, String.class);
  }
}

