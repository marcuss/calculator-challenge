package pro.marcuss.calculator.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.marcuss.calculator.service.dto.RecordDTO;

/**
 * Service Interface for managing {@link pro.marcuss.calculator.domain.Record}.
 */
public interface RecordService {
    /**
     * Save a record.
     *
     * @param recordDTO the entity to save.
     * @return the persisted entity.
     */
    RecordDTO save(RecordDTO recordDTO);

    /**
     * Updates a record.
     *
     * @param recordDTO the entity to update.
     * @return the persisted entity.
     */
    RecordDTO update(RecordDTO recordDTO);

    /**
     * Partially updates a record.
     *
     * @param recordDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RecordDTO> partialUpdate(RecordDTO recordDTO);

    /**
     * Get all the records.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecordDTO> findAll(Pageable pageable);

    /**
     * Get the "id" record.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecordDTO> findOne(String id);

    /**
     * Delete the "id" record.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
