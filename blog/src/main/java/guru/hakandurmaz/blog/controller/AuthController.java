package guru.hakandurmaz.blog.controller;

import guru.hakandurmaz.blog.payload.security.JwtAuthResponse;
import guru.hakandurmaz.blog.payload.security.LoginRequest;
import guru.hakandurmaz.blog.payload.security.PasswordDto;
import guru.hakandurmaz.blog.payload.security.SignupRequest;
import guru.hakandurmaz.blog.service.AuthService;
import guru.hakandurmaz.blog.utils.results.DataResult;
import guru.hakandurmaz.blog.utils.results.Result;
import guru.hakandurmaz.blog.utils.results.SuccessDataResult;
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

  @PostMapping("/signin")
  public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
    String responseToken = authService.getToken(loginRequest);
    return ResponseEntity.ok(new JwtAuthResponse(responseToken));
  }

  @PostMapping("/signup")
  public ResponseEntity<String> registerUser(@RequestBody SignupRequest signupRequest) {
    return new ResponseEntity<>(authService.registerUser(signupRequest), HttpStatus.OK);
  }

  @PostMapping("/user/resetPassword")
  public DataResult resetPassword(@RequestParam("email") String userEmail) {
    return new DataResult(authService.resetPassword(userEmail), true);
  }

  @PostMapping("/user/savePassword")
  public Result savePassword(@RequestBody PasswordDto passwordDto) {
    return new SuccessDataResult(authService.updateUserPassword(passwordDto));
  }
}
