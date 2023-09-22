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

    User user =
        userRepository
            .findByEmail(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException("Username not found with username: " + username));

    return user;
  }

  @Override
  public User getUserByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(
            () -> new UsernameNotFoundException("Username not found with username: " + email));
  }
}
