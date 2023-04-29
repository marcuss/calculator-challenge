package pro.marcuss.calculator.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pro.marcuss.calculator.domain.UserBalance;

/**
 * Spring Data MongoDB repository for the UserBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserBalanceRepository extends MongoRepository<UserBalance, String> {}
