package guru.hakandurmaz.checker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(
    basePackages = "guru.hakandurmaz.clients"
)
public class CheckerApplication {

  public static void main(String[] args) {
    SpringApplication.run(CheckerApplication.class, args);
  }
}
