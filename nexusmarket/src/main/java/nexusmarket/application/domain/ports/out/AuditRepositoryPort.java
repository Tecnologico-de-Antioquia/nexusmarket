package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.AuditLog;

import java.util.List;

public interface AuditRepositoryPort {

    AuditLog save(AuditLog auditLog);

    List<AuditLog> findAll();

    List<AuditLog> findByPerformedByUserId(String userId);

    List<AuditLog> findByAffectedId(String affectedId);
}
