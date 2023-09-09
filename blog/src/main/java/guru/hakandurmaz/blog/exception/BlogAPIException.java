package guru.hakandurmaz.blog.exception;

import org.springframework.http.HttpStatus;

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

    public BlogAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
