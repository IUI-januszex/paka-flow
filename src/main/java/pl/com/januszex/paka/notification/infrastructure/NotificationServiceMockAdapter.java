package pl.com.januszex.paka.notification.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.com.januszex.paka.notification.api.NotificationServicePort;
import pl.com.januszex.paka.notification.domain.NotificationData;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("!prod")
class NotificationServiceMockAdapter implements NotificationServicePort {

    @Value("${services.notificationQueue}")
    private String queueName;

    @Override
    public void sendNotification(NotificationData notificationData) {
        log.info("----------NOTIFICATION-----------");
        log.info("data: {}", notificationData);
        log.info("---------------------------------");
    }
}
