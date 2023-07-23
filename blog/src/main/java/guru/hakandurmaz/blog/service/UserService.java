package guru.hakandurmaz.blog.service;

import guru.hakandurmaz.blog.entity.User;

public interface UserService {
  User getUserByUserName(String username);

  User getUserByEmail(String username);
}
