package guru.hakandurmaz.checker;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailCheckHistoryRepository extends JpaRepository<MailCheckHistory, Integer> {

  Optional<MailCheckHistory> findByMail(String mail);
}
