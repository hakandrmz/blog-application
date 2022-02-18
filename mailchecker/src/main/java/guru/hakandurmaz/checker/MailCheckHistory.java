package guru.hakandurmaz.checker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;

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
