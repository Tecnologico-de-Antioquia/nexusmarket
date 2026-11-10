package nexusmarket.application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nexusmarket.application.domain.valueobjects.SystemRole;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class AuditLog {

    private String auditId;
    private String operationType;
    private LocalDateTime operationDate = LocalDateTime.now();
    private User performedBy;
    private SystemRole performedByRole;
    private String affectedType;
    private String affectedId;
    private Map<String, Object> details = new HashMap<>();
}
