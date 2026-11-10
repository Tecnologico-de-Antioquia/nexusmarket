package nexusmarket.application.adapters.external;

import nexusmarket.application.domain.ports.out.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingNotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingNotificationAdapter.class);

    @Override
    public void send(String recipient, String subject, String message) {
        log.info("Notificacion para {} | {} | {}", recipient, subject, message);
    }
}
