package guru.hakandurmaz.blog.controller;

import guru.hakandurmaz.blog.payload.security.AuthenticationResponse;
import guru.hakandurmaz.blog.payload.security.LoginRequest;
import guru.hakandurmaz.blog.payload.security.PasswordDto;
import guru.hakandurmaz.blog.payload.security.SignupRequest;
import guru.hakandurmaz.blog.service.AuthService;
import guru.hakandurmaz.blog.utils.results.DataResult;
import guru.hakandurmaz.blog.utils.results.Result;
import guru.hakandurmaz.blog.utils.results.SuccessDataResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController { 
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/sign-in")
  public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
      return ResponseEntity.ok(authService.authenticate(loginRequest));
  }

  @PostMapping("/sign-up")
  public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody SignupRequest signupRequest) {
    return new ResponseEntity<>(authService.registerUser(signupRequest), HttpStatus.OK);
  }

  @PostMapping("/reset-password")
  public DataResult resetPassword(@RequestParam("email") String userEmail) {
    return new DataResult(authService.resetPassword(userEmail), true);
  }

  @PostMapping("/save-password")
  public Result savePassword(@RequestBody PasswordDto passwordDto) {
    return new SuccessDataResult(authService.updateUserPassword(passwordDto));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    authService.refreshToken(request, response);
  }
}
