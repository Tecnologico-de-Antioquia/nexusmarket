package nexusmarket.application.domain.services.operation;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.AuditLog;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.AuditRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegisterAuditLogService {

    private final AuditRepositoryPort auditRepositoryPort;

    public AuditLog register(User performedBy, String operationType, String affectedType, String affectedId,
                             Map<String, Object> details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setOperationType(operationType);
        auditLog.setOperationDate(LocalDateTime.now());
        auditLog.setPerformedBy(performedBy);
        auditLog.setPerformedByRole(performedBy == null ? null : performedBy.getRole());
        auditLog.setAffectedType(affectedType);
        auditLog.setAffectedId(affectedId);
        if (details != null) {
            auditLog.setDetails(new HashMap<>(details));
        }
        return auditRepositoryPort.save(auditLog);
    }
}
