package guru.hakandurmaz.notification;

public record NotificationRequest(Long toUserId, String toUserName, String message) {}
