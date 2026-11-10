package nexusmarket.application.adapters.persistence.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String> {

    List<AuditLogDocument> findByPerformedByUserId(String userId);

    List<AuditLogDocument> findByAffectedId(String affectedId);
}
