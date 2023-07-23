package guru.hakandurmaz.blog.service.impl;

import guru.hakandurmaz.amqp.RabbitMQMessageProducer;
import guru.hakandurmaz.blog.entity.Role;
import guru.hakandurmaz.blog.entity.User;
import guru.hakandurmaz.blog.exception.BlogAPIException;
import guru.hakandurmaz.blog.payload.notification.NotificationRequest;
import guru.hakandurmaz.blog.payload.security.LoginRequest;
import guru.hakandurmaz.blog.payload.security.SignupRequest;
import guru.hakandurmaz.blog.repository.RoleRepository;
import guru.hakandurmaz.blog.repository.UserRepository;
import guru.hakandurmaz.blog.security.JwtTokenProvider;
import guru.hakandurmaz.blog.service.AuthService;
import java.util.Collections;
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
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final RabbitMQMessageProducer rabbitMQMessageProducer;

  @Override
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
}
