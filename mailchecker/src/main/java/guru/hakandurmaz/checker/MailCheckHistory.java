package guru.hakandurmaz.checker;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MailCheckHistory {

  @Id
  @SequenceGenerator(
      name = "mail_id_sequence",
      sequenceName = "mail_id_sequence"
  )
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "mail_id_sequence"
  )
  private Integer id;
  private String mail;
  private boolean isIllegal;
  private LocalDateTime createdAt;

}
