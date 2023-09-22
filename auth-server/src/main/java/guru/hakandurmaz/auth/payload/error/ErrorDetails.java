package guru.hakandurmaz.auth.payload.error;

import java.util.Date;

public record ErrorDetails(Date timestamp, String message, String details) {}
