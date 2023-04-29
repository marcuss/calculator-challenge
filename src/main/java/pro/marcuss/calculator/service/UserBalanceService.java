package pro.marcuss.calculator.service;

import java.util.List;
import java.util.Optional;
import pro.marcuss.calculator.service.dto.UserBalanceDTO;

/**
 * Service Interface for managing {@link pro.marcuss.calculator.domain.UserBalance}.
 */
public interface UserBalanceService {
    /**
     * Save a userBalance.
     *
     * @param userBalanceDTO the entity to save.
     * @return the persisted entity.
     */
    UserBalanceDTO save(UserBalanceDTO userBalanceDTO);

    /**
     * Updates a userBalance.
     *
     * @param userBalanceDTO the entity to update.
     * @return the persisted entity.
     */
    UserBalanceDTO update(UserBalanceDTO userBalanceDTO);

    /**
     * Partially updates a userBalance.
     *
     * @param userBalanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserBalanceDTO> partialUpdate(UserBalanceDTO userBalanceDTO);

    /**
     * Get all the userBalances.
     *
     * @return the list of entities.
     */
    List<UserBalanceDTO> findAll();

    /**
     * Get the "id" userBalance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserBalanceDTO> findOne(String id);

    /**
     * Delete the "id" userBalance.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
