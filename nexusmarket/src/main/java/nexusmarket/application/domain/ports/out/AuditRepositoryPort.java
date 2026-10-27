package nexusmarket.application.domain.ports.out;

import java.util.List;

public interface AuditRepositoryPort {
    void save(String eventType, String description);
    List<String> findRecentEvents(int limit);
}
