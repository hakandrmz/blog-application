package guru.hakandurmaz.auth.service;

import guru.hakandurmaz.auth.entity.User;

public interface UserService {
  User getUserByUserName(String username);

  User getUserByEmail(String username);
}
