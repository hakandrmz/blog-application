package guru.hakandurmaz.clients.notification;

public record NotificationRequest(
        Long toUserId,
        String toUserName,
        String message
) {
}
