package com.hakandurmaz.apigw.security;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("fake")
public class FakeApiKeyAuthorizationChecker implements ApiKeyAuthorizationChecker {
  private static final Map<String, List<String>> keys = Map.of("S3CR3T", List.of("BLOGAPP"));

  @Override
  public boolean isAuthorized(String key, String application) {
    return keys.getOrDefault(key, List.of())
            .stream()
            .anyMatch(k -> k.contains(application));
  }
}
