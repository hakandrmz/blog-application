package guru.hakandurmaz.notification.rabbitmq;

import guru.hakandurmaz.clients.notification.NotificationRequest;
import guru.hakandurmaz.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(NotificationRequest notificationRequest) {
        log.info("Consumed for queue " , notificationRequest);
        notificationService.send(notificationRequest);
    }
}
