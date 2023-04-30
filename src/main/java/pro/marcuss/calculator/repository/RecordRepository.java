package pro.marcuss.calculator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pro.marcuss.calculator.domain.Record;

/**
 * Spring Data MongoDB repository for the Record entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecordRepository extends MongoRepository<Record, String> {

    String LAST_OPERATION_RESPONSE_BY_USER = "lastOperationResponseByUser";

    Page<Record> findAllByActiveIsTrueOrderByDateDesc(Pageable pageable);
}
