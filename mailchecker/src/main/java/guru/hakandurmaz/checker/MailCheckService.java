package guru.hakandurmaz.checker;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MailCheckService {

    private final MailCheckHistoryRepository mailCheckHistoryRepository;

    public boolean isIllegalEmail(String mail) {
        Optional<MailCheckHistory> mailCheckHistory = mailCheckHistoryRepository.findByMail(mail);
        if(mailCheckHistory.isPresent()) {
            if (mailCheckHistory.get().isIllegal()) {
                return true;
            }
        }else {
            mailCheckHistoryRepository.save(
                    MailCheckHistory.builder()
                            .mail(mail)
                            .createdAt(LocalDateTime.now())
                            .isIllegal(false).build()
            );
        }
        return false;
    }
}
