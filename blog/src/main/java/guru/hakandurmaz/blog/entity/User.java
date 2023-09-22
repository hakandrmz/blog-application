package guru.hakandurmaz.blog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
    name = "users",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = {"username"}),
      @UniqueConstraint(columnNames = {"email"})
    })
public class User extends AbstractEntity {

  private String name;
  private String username;
  private String email;
  private String password;
}
