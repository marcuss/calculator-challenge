package pro.marcuss.calculator.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pro.marcuss.calculator.domain.Authority;

/**
 * Spring Data MongoDB repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends MongoRepository<Authority, String> {}
