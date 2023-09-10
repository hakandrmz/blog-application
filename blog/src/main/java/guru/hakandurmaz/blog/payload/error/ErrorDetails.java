package guru.hakandurmaz.blog.payload.error;

import java.util.Date;

public record ErrorDetails(Date timestamp, String message, String details) {}
