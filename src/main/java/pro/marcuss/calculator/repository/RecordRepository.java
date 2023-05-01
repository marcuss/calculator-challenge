package pro.marcuss.calculator.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pro.marcuss.calculator.domain.Record;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Record entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecordRepository extends MongoRepository<Record, String> {

    String LAST_OPERATION_RESPONSE_BY_LOGIN = "lastOperationResponseByLogin";

    Page<Record> findAllByActiveIsTrueOrderByDateDesc(Pageable pageable);

    List<Record> findAllByActiveIsTrueOrderByDateDesc();

    @Cacheable(cacheNames = LAST_OPERATION_RESPONSE_BY_LOGIN)
    Record findFirstByUserLoginAndActiveIsTrueOrderByDateDesc(String userLogin);

}
