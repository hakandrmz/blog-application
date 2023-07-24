package guru.hakandurmaz.blog.service;

import guru.hakandurmaz.blog.entity.PasswordResetToken;
import guru.hakandurmaz.blog.entity.User;
import guru.hakandurmaz.blog.payload.security.LoginRequest;
import guru.hakandurmaz.blog.payload.security.PasswordDto;
import guru.hakandurmaz.blog.payload.security.SignupRequest;

public interface AuthService {

  String registerUser(SignupRequest signupRequest);

  String getToken(LoginRequest loginRequest);

  String resetPassword(String userEmail);

  PasswordResetToken validatePasswordResetToken(String token);

  void changeUserPassword(User user, String newPassword);

  String updateUserPassword(PasswordDto passwordDto);
}
