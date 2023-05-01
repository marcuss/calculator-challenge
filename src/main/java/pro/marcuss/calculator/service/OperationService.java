package pro.marcuss.calculator.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pro.marcuss.calculator.domain.enumeration.Operator;
import pro.marcuss.calculator.service.dto.OperationDTO;

import java.util.Optional;

/**
 * Service Interface for managing {@link pro.marcuss.calculator.domain.Operation}.
 */
public interface OperationService {
    /**
     * Save a operation.
     *
     * @param operationDTO the entity to save.
     * @return the persisted entity.
     */
    OperationDTO save(OperationDTO operationDTO);

    /**
     * Updates a operation.
     *
     * @param operationDTO the entity to update.
     * @return the persisted entity.
     */
    OperationDTO update(OperationDTO operationDTO);

    /**
     * Partially updates a operation.
     *
     * @param operationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OperationDTO> partialUpdate(OperationDTO operationDTO);

    /**
     * Get all the operations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OperationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" operation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OperationDTO> findOne(String id);

    /**
     * Get the operation cost by operator.
     *
     * @param operator the operator of the entity.
     * @return the entity.
     */
    Optional<OperationDTO> findOneByOperator(Operator operator);

    /**
     * Delete the "id" operation.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
