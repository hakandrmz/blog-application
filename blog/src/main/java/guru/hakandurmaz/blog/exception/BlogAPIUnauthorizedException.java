package guru.hakandurmaz.blog.exception;

public class BlogAPIUnauthorizedException extends RuntimeException {

  private final String message;

  public BlogAPIUnauthorizedException(String message) {
    this.message = message;
  }
}
