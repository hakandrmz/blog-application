package guru.hakandurmaz.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BlogAPIException extends RuntimeException {

  private HttpStatus status;
  private final String message;

  public BlogAPIException(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  public BlogAPIException(String message) {
    this.message = message;
  }
}
