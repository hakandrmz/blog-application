package guru.hakandurmaz.auth.exception;

public class BlogAPIUnauthorizedException extends RuntimeException {

  public BlogAPIUnauthorizedException(String message) {
    super(message);
  }
}
