package pl.com.januszex.paka.notification.api;

import pl.com.januszex.paka.notification.domain.NotificationData;

public interface NotificationServicePort {

    void sendNotification(NotificationData notificationData);

}
