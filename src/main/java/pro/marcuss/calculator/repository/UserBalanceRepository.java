package pro.marcuss.calculator.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pro.marcuss.calculator.domain.UserBalance;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the UserBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserBalanceRepository extends MongoRepository<UserBalance, String> {
    String BALANCE_BY_USER_ID_CACHE = "balanceByUserId";

    @Cacheable(cacheNames = BALANCE_BY_USER_ID_CACHE)
    Optional<UserBalance> findUserBalanceByUserId(String id);
}
