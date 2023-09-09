package guru.hakandurmaz.blog.service;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.hakandurmaz.blog.exception.BlogAPIException;
import guru.hakandurmaz.blog.utils.mappers.ModelMapperManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.json.JsonParseException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value) {
        try {
            String valueAsString = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, valueAsString);
        } catch (JsonProcessingException exception) {
            throw new BlogAPIException("An error occurred while json processing.");
        }
    }
}
