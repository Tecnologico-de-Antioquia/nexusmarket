package nexusmarket.application.infrastructure.security;

import org.springframework.stereotype.Service;

import nexusmarket.application.domain.ports.out.NotificationPort;

@Service
public class ConsoleNotificationService implements NotificationPort {
    @Override
    public void send(String recipient, String subject, String message) {
        System.out.println("Notification to " + recipient + " | " + subject + " | " + message);
    }
}
