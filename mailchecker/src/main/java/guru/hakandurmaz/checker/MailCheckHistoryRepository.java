package guru.hakandurmaz.checker;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailCheckHistoryRepository extends JpaRepository<MailCheckHistory,Integer> {
    Optional<MailCheckHistory> findByMail(String mail);
}
