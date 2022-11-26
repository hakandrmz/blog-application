package guru.hakandurmaz.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = {
        "guru.hakandurmaz.notification",
        "guru.hakandurmaz.amqp"
    }
)
public class NotificationApplication {

  public static void main(String[] args) {
    SpringApplication.run(NotificationApplication.class, args);
  }

  //@Bean
  //CommandLineRunner commandLineRunner(RabbitMQMessageProducer producer,
  //                                    NotificationConfig notificationConfig) {
  //    return args -> {
  //        producer.publish(new Person("Hakan",2),
  //                notificationConfig.getInternalExchange(),
  //                notificationConfig.getInternalNotificationRoutingKey());
  //    };
//
//
  //}
//
  //record Person(String name,int age) {}
}