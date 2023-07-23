package guru.hakandurmaz.blog.payload.notification;

public record NotificationRequest(Long toUserId, String toUserName, String message) {}
