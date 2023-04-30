package pro.marcuss.calculator.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pro.marcuss.calculator.domain.Operation;
import pro.marcuss.calculator.domain.enumeration.Operator;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Operation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperationRepository extends MongoRepository<Operation, String> {

    String COST_BY_OPERATOR_CACHE = "costByOperator";

    @Cacheable(cacheNames = COST_BY_OPERATOR_CACHE)
    Optional<Operation> findOneByOperator(Operator operator);
}
