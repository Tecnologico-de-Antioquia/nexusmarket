package nexusmarket.application.adapters.persistence.mongodb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
public class AuditLogDocument {

    @Id
    private String id;
    private String operationType;
    private LocalDateTime operationDate;
    private String performedByUserId;
    private String performedByUsername;
    private String performedByRole;
    private String affectedType;
    private String affectedId;
    private Map<String, Object> details = new HashMap<>();
}
