package guru.hakandurmaz.checker;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MailCheckService {

    private final MailCheckHistoryRepository mailCheckHistoryRepository;

    public boolean isIllegalEmail(String mail) {
        mailCheckHistoryRepository.save(
                MailCheckHistory.builder()
                        .mail(mail).
                        createdAt(LocalDateTime.now())
                        .isIllegal(false).build()
        );
        return false;
    }

}
