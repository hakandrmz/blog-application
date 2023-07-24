package guru.hakandurmaz.blog.service.impl;

import guru.hakandurmaz.amqp.RabbitMQMessageProducer;
import guru.hakandurmaz.blog.entity.PasswordResetToken;
import guru.hakandurmaz.blog.entity.Role;
import guru.hakandurmaz.blog.entity.User;
import guru.hakandurmaz.blog.exception.BlogAPIException;
import guru.hakandurmaz.blog.payload.notification.NotificationRequest;
import guru.hakandurmaz.blog.payload.security.LoginRequest;
import guru.hakandurmaz.blog.payload.security.PasswordDto;
import guru.hakandurmaz.blog.payload.security.SignupRequest;
import guru.hakandurmaz.blog.repository.PasswordTokenRepository;
import guru.hakandurmaz.blog.repository.RoleRepository;
import guru.hakandurmaz.blog.repository.UserRepository;
import guru.hakandurmaz.blog.security.JwtTokenProvider;
import guru.hakandurmaz.blog.service.AuthService;
import guru.hakandurmaz.blog.service.UserService;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final AuthenticationManager authenticationManager;
  private final PasswordTokenRepository passwordTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;
  private final RabbitMQMessageProducer rabbitMQMessageProducer;

  @Override
  @Transactional
  public String registerUser(SignupRequest signupRequest) {
    if (Boolean.TRUE.equals(userRepository.existsByUsername(signupRequest.getUsername()))) {
      return "Username is already taken!";
    } else if (Boolean.TRUE.equals(userRepository.existsByEmail(signupRequest.getEmail()))) {
      return "Email is already taken!";
    } else {
      User user = new User();
      user.setEmail(signupRequest.getEmail());
      user.setUsername(signupRequest.getUsername());
      user.setName(signupRequest.getName());
      user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

      Role roles =
          roleRepository
              .findByName("ROLE_USER")
              .orElseThrow(() -> new BlogAPIException(HttpStatus.BAD_REQUEST, "Role Not found."));
      user.setRoles(Collections.singleton(roles));

      userRepository.saveAndFlush(user);

      NotificationRequest notificationRequest =
          new NotificationRequest(
              user.getId(), user.getEmail(), "Welcome to my blog" + user.getName());

      rabbitMQMessageProducer.publish(
          notificationRequest, "internal.exchange", "internal.notification.routing-key");
    }
    return "User registered";
  }

  @Override
  @Transactional
  public String getToken(LoginRequest loginRequest) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    return jwtTokenProvider.generateToken(authentication);
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
  public String updateUserPassword(PasswordDto passwordDto) {
    PasswordResetToken passwordResetToken = validatePasswordResetToken(passwordDto.getToken());
    changeUserPassword(passwordResetToken.getUser(), passwordDto.getNewPassword());
    passwordTokenRepository.deleteByToken(passwordDto.getToken());
    return "Password changed.";
  }

  private void checkTokenDate(PasswordResetToken passToken) {
    final Calendar cal = Calendar.getInstance();

    if (passToken.getExpiryDate().before(cal.getTime()))
      throw new BlogAPIException(HttpStatus.BAD_GATEWAY, "Token expired.");
  }

  public PasswordResetToken validatePasswordResetToken(String token) {
    final PasswordResetToken passToken =
        passwordTokenRepository
            .findByToken(token)
            .orElseThrow(() -> new BlogAPIException(HttpStatus.NOT_FOUND, "Token not found."));
    checkTokenDate(passToken);
    return passToken;
  }

  private static Date getExpireDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.HOUR_OF_DAY, 1);
    return calendar.getTime();
  }
}
