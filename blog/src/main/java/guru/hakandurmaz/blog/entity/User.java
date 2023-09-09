package guru.hakandurmaz.blog.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
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
public class User extends AbstractEntity  implements Serializable {

  private String name;
  private String username;
  private String email;
  private String password;

  @JsonManagedReference
  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<Role> roles;
  
}
