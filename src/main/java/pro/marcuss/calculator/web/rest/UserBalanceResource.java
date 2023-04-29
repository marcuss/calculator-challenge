package pro.marcuss.calculator.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.marcuss.calculator.repository.UserBalanceRepository;
import pro.marcuss.calculator.service.UserBalanceService;
import pro.marcuss.calculator.service.dto.UserBalanceDTO;
import pro.marcuss.calculator.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pro.marcuss.calculator.domain.UserBalance}.
 */
@RestController
@RequestMapping("/api/v1")
public class UserBalanceResource {

    private final Logger log = LoggerFactory.getLogger(UserBalanceResource.class);

    private static final String ENTITY_NAME = "userBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserBalanceService userBalanceService;

    private final UserBalanceRepository userBalanceRepository;

    public UserBalanceResource(UserBalanceService userBalanceService, UserBalanceRepository userBalanceRepository) {
        this.userBalanceService = userBalanceService;
        this.userBalanceRepository = userBalanceRepository;
    }

    /**
     * {@code POST  /user-balances} : Create a new userBalance.
     *
     * @param userBalanceDTO the userBalanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userBalanceDTO, or with status {@code 400 (Bad Request)} if the userBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-balances")
    public ResponseEntity<UserBalanceDTO> createUserBalance(@Valid @RequestBody UserBalanceDTO userBalanceDTO) throws URISyntaxException {
        log.debug("REST request to save UserBalance : {}", userBalanceDTO);
        if (userBalanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new userBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserBalanceDTO result = userBalanceService.save(userBalanceDTO);
        return ResponseEntity
            .created(new URI("/api/v1/user-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /user-balances/:id} : Updates an existing userBalance.
     *
     * @param id the id of the userBalanceDTO to save.
     * @param userBalanceDTO the userBalanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userBalanceDTO,
     * or with status {@code 400 (Bad Request)} if the userBalanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userBalanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-balances/{id}")
    public ResponseEntity<UserBalanceDTO> updateUserBalance(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody UserBalanceDTO userBalanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserBalance : {}, {}", id, userBalanceDTO);
        if (userBalanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userBalanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userBalanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserBalanceDTO result = userBalanceService.update(userBalanceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userBalanceDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-balances/:id} : Partial updates given fields of an existing userBalance, field will ignore if it is null
     *
     * @param id the id of the userBalanceDTO to save.
     * @param userBalanceDTO the userBalanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userBalanceDTO,
     * or with status {@code 400 (Bad Request)} if the userBalanceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userBalanceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userBalanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-balances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserBalanceDTO> partialUpdateUserBalance(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody UserBalanceDTO userBalanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserBalance partially : {}, {}", id, userBalanceDTO);
        if (userBalanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userBalanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userBalanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserBalanceDTO> result = userBalanceService.partialUpdate(userBalanceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userBalanceDTO.getId())
        );
    }

    /**
     * {@code GET  /user-balances} : get all the userBalances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userBalances in body.
     */
    @GetMapping("/user-balances")
    public List<UserBalanceDTO> getAllUserBalances() {
        log.debug("REST request to get all UserBalances");
        return userBalanceService.findAll();
    }

    /**
     * {@code GET  /user-balances/:id} : get the "id" userBalance.
     *
     * @param id the id of the userBalanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userBalanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-balances/{id}")
    public ResponseEntity<UserBalanceDTO> getUserBalance(@PathVariable String id) {
        log.debug("REST request to get UserBalance : {}", id);
        Optional<UserBalanceDTO> userBalanceDTO = userBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userBalanceDTO);
    }

    /**
     * {@code DELETE  /user-balances/:id} : delete the "id" userBalance.
     *
     * @param id the id of the userBalanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-balances/{id}")
    public ResponseEntity<Void> deleteUserBalance(@PathVariable String id) {
        log.debug("REST request to delete UserBalance : {}", id);
        userBalanceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}