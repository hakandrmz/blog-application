package guru.hakandurmaz.auth.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "roles")
public class Role extends AbstractEntity {

  @Column(length = 60)
  private String name;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  @JsonBackReference
  private List<User> users;
}
