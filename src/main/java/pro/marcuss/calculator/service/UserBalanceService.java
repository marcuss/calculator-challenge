package pro.marcuss.calculator.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserBalanceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userBalance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserBalanceDTO> findOne(String id);

    Optional<UserBalanceDTO> findUserBalanceByUserLogin(String login);

    /**
     * Delete the "id" userBalance.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
