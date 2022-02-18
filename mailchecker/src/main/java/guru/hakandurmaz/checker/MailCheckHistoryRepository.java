package guru.hakandurmaz.checker;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MailCheckHistoryRepository extends JpaRepository<MailCheckHistory,Integer> {
}
