package guru.hakandurmaz.auth.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.hakandurmaz.amqp.AmqpConstants;
import guru.hakandurmaz.amqp.RabbitMQMessageProducer;
import guru.hakandurmaz.auth.entity.PasswordResetToken;
import guru.hakandurmaz.auth.entity.Role;
import guru.hakandurmaz.auth.entity.Token;
import guru.hakandurmaz.auth.entity.User;
import guru.hakandurmaz.auth.exception.BlogAPIException;
import guru.hakandurmaz.auth.payload.notification.NotificationRequest;
import guru.hakandurmaz.auth.payload.security.*;
import guru.hakandurmaz.auth.repository.PasswordTokenRepository;
import guru.hakandurmaz.auth.repository.RoleRepository;
import guru.hakandurmaz.auth.repository.TokenRepository;
import guru.hakandurmaz.auth.repository.UserRepository;
import guru.hakandurmaz.auth.security.JwtTokenProvider;
import guru.hakandurmaz.auth.service.AuthService;
import guru.hakandurmaz.auth.service.UserService;
import guru.hakandurmaz.auth.utils.constants.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final TokenRepository tokenRepository;
  private final AuthenticationManager authenticationManager;
  private final PasswordTokenRepository passwordTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;
  private final RabbitMQMessageProducer rabbitMQMessageProducer;

  @Override
  @Transactional
  public AuthenticationResponse registerUser(SignupRequest signupRequest) {
    if (Boolean.TRUE.equals(userRepository.existsByUsername(signupRequest.getUsername()))) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, AppConstants.USERNAME_ALREADY_EXIST);
    } else if (Boolean.TRUE.equals(userRepository.existsByEmail(signupRequest.getEmail()))) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, AppConstants.EMAIL_ALREADY_EXIST);
    }
    User user = generateUserFromRequest(signupRequest);

    User savedUser = userRepository.save(user);
    String accessToken = jwtTokenProvider.generateToken(user);
    String refreshToken = jwtTokenProvider.generateRefreshToken(user);
    saveUserToken(savedUser, accessToken);

    NotificationRequest notificationRequest =
        new NotificationRequest(
            user.getId(), user.getEmail(), "Welcome to my blog" + user.getName());

    rabbitMQMessageProducer.publish(
        notificationRequest,
        AmqpConstants.INTERNAL_EXCHANGE,
        AmqpConstants.INTERNAL_NOTIFICATION_ROUTING_KEY);

    return AuthenticationResponse.builder()
        .tokenType(AppConstants.BEARER)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  @Override
  @Transactional
  public AuthenticationResponse authenticate(LoginRequest request) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsernameOrEmail(), request.getPassword()));
    var user = userRepository.findByEmail(request.getUsernameOrEmail()).orElseThrow();
    var jwtToken = jwtTokenProvider.generateToken(user);
    var refreshToken = jwtTokenProvider.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .tokenType(AppConstants.BEARER_)
        .build();
  }

  @Override
  @Transactional
  public String resetPassword(String userEmail) {
    User user = userService.getUserByEmail(userEmail);
    String token = UUID.randomUUID().toString();
    Date expireDate = getExpireDate();
    PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, expireDate);
    passwordTokenRepository.save(passwordResetToken);
    return token;
  }

  @Override
  @Transactional
  public void changeUserPassword(User user, String password) {
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void updateUserPassword(PasswordDto passwordDto) {
    PasswordResetToken passwordResetToken = validatePasswordResetToken(passwordDto.getToken());
    changeUserPassword(passwordResetToken.getUser(), passwordDto.getNewPassword());
    passwordTokenRepository.deleteByToken(passwordDto.getToken());
  }

  private void checkTokenDate(PasswordResetToken passToken) {
    final Calendar cal = Calendar.getInstance();

    if (passToken.getExpiryDate().before(cal.getTime()))
      throw new BlogAPIException(HttpStatus.BAD_GATEWAY, AppConstants.TOKEN_EXPIRED);
  }

  public PasswordResetToken validatePasswordResetToken(String token) {
    final PasswordResetToken passToken =
        passwordTokenRepository
            .findByToken(token)
            .orElseThrow(
                () -> new BlogAPIException(HttpStatus.NOT_FOUND, AppConstants.TOKEN_NOT_FOUND));
    checkTokenDate(passToken);
    return passToken;
  }

  private static Date getExpireDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.HOUR_OF_DAY, 1);
    return calendar.getTime();
  }

  public void refreshToken(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith(AppConstants.BEARER_)) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtTokenProvider.extractUsername(refreshToken);
    if (userEmail != null) {

      User user =
          userRepository
              .findByEmail(userEmail)
              .orElseThrow(
                  () -> new BlogAPIException(HttpStatus.BAD_REQUEST, AppConstants.USER_NOT_FOUND));

      if (jwtTokenProvider.isTokenValid(refreshToken, user)) {
        var accessToken = jwtTokenProvider.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse =
            AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty()) return;
    validUserTokens.forEach(
        token -> {
          token.setExpired(true);
          token.setRevoked(true);
        });
    tokenRepository.saveAll(validUserTokens);
  }

  private void saveUserToken(User user, String jwtToken) {
    var token =
        Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  private User generateUserFromRequest(SignupRequest signupRequest) {
    User user = new User();
    user.setEmail(signupRequest.getEmail());
    user.setUsername(signupRequest.getUsername());
    user.setName(signupRequest.getName());
    user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

    Role roles =
        roleRepository
            .findByName(AppConstants.ROLE_USER)
            .orElseThrow(
                () -> new BlogAPIException(HttpStatus.BAD_REQUEST, AppConstants.ROLE_NOT_FOUND));
    user.setRoles(Collections.singleton(roles));
    return user;
  }
}
