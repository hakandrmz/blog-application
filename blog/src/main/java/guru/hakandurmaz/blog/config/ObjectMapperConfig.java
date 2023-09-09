package guru.hakandurmaz.blog.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Configuration
public class ObjectMapperConfig {
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
    objectMapper.setDateFormat(df);
    return objectMapper;
  }
}
