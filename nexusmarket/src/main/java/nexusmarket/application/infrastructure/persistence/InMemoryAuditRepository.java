package nexusmarket.application.infrastructure.persistence;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.springframework.stereotype.Repository;

import nexusmarket.application.domain.ports.out.AuditRepositoryPort;

@Repository
public class InMemoryAuditRepository implements AuditRepositoryPort {
    private final Deque<String> events = new ArrayDeque<>();

    @Override
    public void save(String eventType, String description) {
        String event = eventType + ": " + description;
        events.addFirst(event);
    }

    @Override
    public List<String> findRecentEvents(int limit) {
        List<String> result = new ArrayList<>();
        int count = 0;
        for (String event : events) {
            if (count >= limit) {
                break;
            }
            result.add(event);
            count++;
        }
        return result;
    }
}
