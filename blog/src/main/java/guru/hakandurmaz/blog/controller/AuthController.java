package guru.hakandurmaz.blog.controller;

import guru.hakandurmaz.blog.config.LogPerformance;
import guru.hakandurmaz.blog.payload.security.AuthenticationResponse;
import guru.hakandurmaz.blog.payload.security.LoginRequest;
import guru.hakandurmaz.blog.payload.security.PasswordDto;
import guru.hakandurmaz.blog.payload.security.SignupRequest;
import guru.hakandurmaz.blog.service.AuthService;
import guru.hakandurmaz.blog.utils.results.DataResult;
import guru.hakandurmaz.blog.utils.results.Result;
import guru.hakandurmaz.blog.utils.results.SuccessDataResult;
import guru.hakandurmaz.blog.utils.results.SuccessResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController { 
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @LogPerformance
  @PostMapping("/sign-in")
  public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
      return ResponseEntity.ok(authService.authenticate(loginRequest));
  }

  @LogPerformance
  @PostMapping("/sign-up")
  public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody SignupRequest signupRequest) {
    return new ResponseEntity<>(authService.registerUser(signupRequest), HttpStatus.OK);
  }

  @LogPerformance
  @PostMapping("/reset-password")
  public DataResult resetPassword(@RequestParam("email") String userEmail) {
    return new SuccessDataResult(authService.resetPassword(userEmail));
  }

  @LogPerformance
  @PostMapping("/save-password")
  public Result savePassword(@RequestBody PasswordDto passwordDto) {
    return new SuccessDataResult(authService.updateUserPassword(passwordDto));
  }

  @LogPerformance
  @PostMapping("/refresh-token")
  public Result refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    authService.refreshToken(request, response);
    return new SuccessResult();
  }
}
