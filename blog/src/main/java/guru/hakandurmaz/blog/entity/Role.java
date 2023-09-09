package guru.hakandurmaz.blog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "roles")
public class Role extends AbstractEntity implements Serializable {

  @Column(length = 60)
  private String name;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  @JsonBackReference
  private List<User> users;
}
