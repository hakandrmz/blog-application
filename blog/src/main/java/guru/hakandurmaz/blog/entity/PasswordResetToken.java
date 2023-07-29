package guru.hakandurmaz.blog.entity;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;

@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "password_reset_tokens")
public class PasswordResetToken extends AbstractEntity {

  private final String token;

  @OneToOne(targetEntity = User.class)
  @JoinColumn(nullable = false, name = "user_id")
  private final User user;

  private final Date expiryDate;
}
