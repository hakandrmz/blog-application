package com.hakandurmaz.apigw.security;

public interface ApiKeyAuthorizationChecker {
  boolean isAuthorized(String key, String application);
}
