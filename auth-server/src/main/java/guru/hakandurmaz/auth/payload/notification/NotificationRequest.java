package guru.hakandurmaz.auth.payload.notification;

public record NotificationRequest(Long toUserId, String toUserName, String message) {}
