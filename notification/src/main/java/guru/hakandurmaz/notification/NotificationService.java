package guru.hakandurmaz.notification;

import guru.hakandurmaz.clients.notification.NotificationRequest;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;

  public void send(NotificationRequest notificationRequest) {
    notificationRepository.save(
        Notification.builder()
            .toUserId(notificationRequest.toUserId())
            .toUserEmail(notificationRequest.toUserName())
            .sender("Hakan Durmaz")
            .message(notificationRequest.message())
            .sentAt(LocalDateTime.now())
            .build()
    );
  }
}
