package io.houthacker.notifications.backend.notifications;

/**
 * @author houthacker
 */
public interface Notification {

    /**
     * Returns the source backend of the notification.
     *
     * @return The notification source
     */
    default NotificationSource getSource() {
        return NotificationSource.unknown;
    }

    /**
     * Returns the notification type (implementation specific).
     *
     * @return The notification type.
     */
    String getType();

    /**
     * Returns the UTC timestamp of when the notification occurred.
     *
     * @return The timestamp of the notification
     */
    long getTimestamp();
}
