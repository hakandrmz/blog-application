package guru.hakandurmaz.blog.payload.error;

import java.util.Date;

public class ErrorDetails {

  private final Date timestamp;
  private final String message;
  private final String details;

  public ErrorDetails(Date timestamp, String message, String details) {
    this.timestamp = timestamp;
    this.message = message;
    this.details = details;
  }

  public String getMessage() {
    return message;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getDetails() {
    return details;
  }
}
