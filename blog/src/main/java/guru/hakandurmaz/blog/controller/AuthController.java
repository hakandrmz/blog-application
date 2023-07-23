package guru.hakandurmaz.blog.controller;

import guru.hakandurmaz.blog.payload.security.JwtAuthResponse;
import guru.hakandurmaz.blog.payload.security.LoginRequest;
import guru.hakandurmaz.blog.payload.security.SignupRequest;
import guru.hakandurmaz.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/signin")
  public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
    String responseToken = authService.getToken(loginRequest);
    return ResponseEntity.ok(new JwtAuthResponse(responseToken));
  }

  @PostMapping("/signup")
  public ResponseEntity<String> registerUser(@RequestBody SignupRequest signupRequest) {
    return new ResponseEntity<>(authService.registerUser(signupRequest), HttpStatus.OK);
  }
}
