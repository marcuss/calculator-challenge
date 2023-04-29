package pro.marcuss.calculator.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pro.marcuss.calculator.domain.Record;

/**
 * Spring Data MongoDB repository for the Record entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecordRepository extends MongoRepository<Record, String> {}
