package pl.com.januszex.paka.notification.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.com.januszex.paka.notification.api.NotificationServicePort;
import pl.com.januszex.paka.notification.domain.NotificationData;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("prod")
class NotificationServiceAdapter implements NotificationServicePort {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${services.notificationQueue}")
    private String queueName;

    @Override
    public void sendNotification(NotificationData notificationData) {
        try {
            String body = objectMapper.writeValueAsString(notificationData);
            rabbitTemplate.convertAndSend(queueName, body);
        } catch (Exception e) {
            log.error("Sending notification", e);
        }
    }
}
