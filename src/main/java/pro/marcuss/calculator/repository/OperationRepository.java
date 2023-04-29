package pro.marcuss.calculator.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pro.marcuss.calculator.domain.Operation;

/**
 * Spring Data MongoDB repository for the Operation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperationRepository extends MongoRepository<Operation, String> {}
