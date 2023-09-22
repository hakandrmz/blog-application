package guru.hakandurmaz.auth.entity;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@MappedSuperclass
@RequiredArgsConstructor
public abstract class AbstractEntity {

  @Id
  @SequenceGenerator(name = "blog_id_sequence", sequenceName = "blog_id_sequence")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blog_id_sequence")
  private long id;

  @CreationTimestamp(source = SourceType.DB)
  private Date createdOn;

  @UpdateTimestamp(source = SourceType.DB)
  private Date lastUpdatedOn;
}
