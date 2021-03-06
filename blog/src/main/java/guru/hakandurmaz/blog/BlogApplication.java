package guru.hakandurmaz.blog;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(
		scanBasePackages = {
				"guru.hakandurmaz.blog",
				"guru.hakandurmaz.amqp"
		}
)
@EnableEurekaClient
@EnableFeignClients(
		basePackages = "guru.hakandurmaz.clients"
)

public class BlogApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

}
