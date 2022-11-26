package guru.hakandurmaz.blog.service;

import guru.hakandurmaz.blog.payload.security.LoginRequest;
import guru.hakandurmaz.blog.payload.security.SignupRequest;

public interface AuthService {

  String registerUser(SignupRequest signupRequest);

  String getToken(LoginRequest loginRequest);
}
