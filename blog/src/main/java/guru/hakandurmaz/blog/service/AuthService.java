package guru.hakandurmaz.blog.service;

import guru.hakandurmaz.blog.entity.PasswordResetToken;
import guru.hakandurmaz.blog.entity.User;
import guru.hakandurmaz.blog.payload.security.AuthenticationResponse;
import guru.hakandurmaz.blog.payload.security.LoginRequest;
import guru.hakandurmaz.blog.payload.security.PasswordDto;
import guru.hakandurmaz.blog.payload.security.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthService {

  AuthenticationResponse registerUser(SignupRequest signupRequest);

  AuthenticationResponse authenticate(LoginRequest loginRequest);

  String resetPassword(String userEmail);

  PasswordResetToken validatePasswordResetToken(String token);

  void changeUserPassword(User user, String newPassword);

  String updateUserPassword(PasswordDto passwordDto);

  void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
