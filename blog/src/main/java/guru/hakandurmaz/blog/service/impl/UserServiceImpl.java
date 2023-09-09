package guru.hakandurmaz.blog.service.impl;

import guru.hakandurmaz.blog.entity.User;
import guru.hakandurmaz.blog.repository.UserRepository;
import guru.hakandurmaz.blog.service.CacheService;
import guru.hakandurmaz.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CacheService cacheService;

    @Override
    public User getUserByUserName(String username) {

        Object user = cacheService.get(username);
        if (Objects.isNull(user)) {
            user = userRepository
                    .findByEmail(username)
                    .orElseThrow(
                            () -> new UsernameNotFoundException("Username not found with username: " + username));
            cacheService.set(username, user);
        }
        return (User) user;

    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Username not found with username: " + email));
    }
}
