package guru.hakandurmaz.blog.service.impl;

import guru.hakandurmaz.blog.entity.User;
import guru.hakandurmaz.blog.repository.UserRepository;
import guru.hakandurmaz.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public User getUserByUserName(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("Username not found with username: " + username));
  }

  @Override
  public User getUserByEmail(String username) {
    return userRepository
        .findByEmail(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("Username not found with username: " + username));
  }

  @Override
  public Boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public Boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public User createUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public User getByEmail(String usernameOrEmail) {
    return userRepository.findByEmail(usernameOrEmail).orElseThrow();
  }
}
