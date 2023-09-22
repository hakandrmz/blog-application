package guru.hakandurmaz.auth.service;

import guru.hakandurmaz.auth.entity.PasswordResetToken;
import guru.hakandurmaz.auth.entity.User;
import guru.hakandurmaz.auth.payload.security.AuthenticationResponse;
import guru.hakandurmaz.auth.payload.security.LoginRequest;
import guru.hakandurmaz.auth.payload.security.PasswordDto;
import guru.hakandurmaz.auth.payload.security.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthService {

  AuthenticationResponse registerUser(SignupRequest signupRequest);

  AuthenticationResponse authenticate(LoginRequest loginRequest);

  String resetPassword(String userEmail);

  PasswordResetToken validatePasswordResetToken(String token);

  void changeUserPassword(User user, String newPassword);

  void updateUserPassword(PasswordDto passwordDto);

  void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
