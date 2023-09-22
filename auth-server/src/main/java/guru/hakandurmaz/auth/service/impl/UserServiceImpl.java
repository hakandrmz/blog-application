package guru.hakandurmaz.auth.service.impl;

import guru.hakandurmaz.auth.entity.User;
import guru.hakandurmaz.auth.repository.UserRepository;
import guru.hakandurmaz.auth.service.UserService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RedisTemplate<String,User> userRedisTemplate;
    @Override
    public User getUserByUserName(String username) {

        User user = userRedisTemplate.opsForValue().get(username);
        if (Objects.isNull(user)) {
            user = userRepository
                    .findByEmail(username)
                    .orElseThrow(
                            () -> new UsernameNotFoundException("Username not found with username: " + username));
            userRedisTemplate.opsForValue().set(username,user);
        }
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
