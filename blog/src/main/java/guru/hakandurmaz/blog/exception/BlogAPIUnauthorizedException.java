package guru.hakandurmaz.blog.exception;

public class BlogAPIUnauthorizedException extends RuntimeException {

  public BlogAPIUnauthorizedException(String message) {
    super(message);
  }
}
