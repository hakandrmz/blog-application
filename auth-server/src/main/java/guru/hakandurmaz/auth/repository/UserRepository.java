package guru.hakandurmaz.auth.repository;

import guru.hakandurmaz.auth.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsernameOrEmail(String username, String email);
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
