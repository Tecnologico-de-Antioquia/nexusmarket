package nexusmarket.application.adapters.persistence.mongodb;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.AuditLog;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.AuditRepositoryPort;
import nexusmarket.application.domain.valueobjects.SystemRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MongoAuditRepositoryAdapter implements AuditRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(MongoAuditRepositoryAdapter.class);

    private final AuditLogMongoRepository repository;

    @Override
    public AuditLog save(AuditLog auditLog) {
        try {
            AuditLogDocument document = toDocument(auditLog);
            AuditLogDocument saved = repository.save(document);
            auditLog.setAuditId(saved.getId());
        } catch (RuntimeException exception) {
            log.warn("No fue posible registrar la auditoria en MongoDB: {}", exception.getMessage());
        }
        return auditLog;
    }

    @Override
    public List<AuditLog> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<AuditLog> findByPerformedByUserId(String userId) {
        return repository.findByPerformedByUserId(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<AuditLog> findByAffectedId(String affectedId) {
        return repository.findByAffectedId(affectedId).stream().map(this::toDomain).toList();
    }

    private AuditLogDocument toDocument(AuditLog auditLog) {
        AuditLogDocument document = new AuditLogDocument();
        document.setId(auditLog.getAuditId() == null || auditLog.getAuditId().isBlank()
                ? UUID.randomUUID().toString() : auditLog.getAuditId());
        document.setOperationType(auditLog.getOperationType());
        document.setOperationDate(auditLog.getOperationDate());
        document.setPerformedByUserId(auditLog.getPerformedBy() == null ? null : auditLog.getPerformedBy().getId());
        document.setPerformedByUsername(auditLog.getPerformedBy() == null
                ? null : auditLog.getPerformedBy().getUsername());
        document.setPerformedByRole(auditLog.getPerformedByRole() == null
                ? null : auditLog.getPerformedByRole().getCode());
        document.setAffectedType(auditLog.getAffectedType());
        document.setAffectedId(auditLog.getAffectedId());
        document.setDetails(auditLog.getDetails());
        return document;
    }

    private AuditLog toDomain(AuditLogDocument document) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAuditId(document.getId());
        auditLog.setOperationType(document.getOperationType());
        auditLog.setOperationDate(document.getOperationDate());
        if (document.getPerformedByUserId() != null || document.getPerformedByUsername() != null) {
            User user = new User();
            user.setId(document.getPerformedByUserId());
            user.setUsername(document.getPerformedByUsername());
            auditLog.setPerformedBy(user);
        }
        auditLog.setPerformedByRole(document.getPerformedByRole() == null
                ? null : SystemRole.fromCode(document.getPerformedByRole()));
        auditLog.setAffectedType(document.getAffectedType());
        auditLog.setAffectedId(document.getAffectedId());
        auditLog.setDetails(document.getDetails());
        return auditLog;
    }
}
