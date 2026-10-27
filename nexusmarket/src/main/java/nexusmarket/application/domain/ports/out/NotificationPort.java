package nexusmarket.application.domain.ports.out;

public interface NotificationPort {
    void send(String recipient, String subject, String message);
}
