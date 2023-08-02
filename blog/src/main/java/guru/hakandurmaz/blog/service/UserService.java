package guru.hakandurmaz.blog.service;

import guru.hakandurmaz.blog.entity.User;

public interface UserService {
  User getUserByUserName(String username);

  User getUserByEmail(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  User createUser(User user);

  User getByEmail(String usernameOrEmail);
}
