package guru.hakandurmaz.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Notification {

  @Id
  @SequenceGenerator(name = "notification_id_sequence", sequenceName = "notification_id_sequence")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_id_sequence")
  private Long notificationId;

  private Long toUserId;
  private String toUserEmail;
  private String sender;
  private String message;
  private LocalDateTime sentAt;

  @CreationTimestamp(source = SourceType.DB)
  private Instant createdOn;

  @UpdateTimestamp(source = SourceType.DB)
  private Instant lastUpdatedOn;
}
