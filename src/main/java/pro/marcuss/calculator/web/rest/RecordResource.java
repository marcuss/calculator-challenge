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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pro.marcuss.calculator.repository.RecordRepository;
import pro.marcuss.calculator.service.RecordService;
import pro.marcuss.calculator.service.dto.RecordDTO;
import pro.marcuss.calculator.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pro.marcuss.calculator.domain.Record}.
 */
@RestController
@RequestMapping("/api/v1")
public class RecordResource {

    private final Logger log = LoggerFactory.getLogger(RecordResource.class);

    private static final String ENTITY_NAME = "record";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecordService recordService;

    private final RecordRepository recordRepository;

    public RecordResource(RecordService recordService, RecordRepository recordRepository) {
        this.recordService = recordService;
        this.recordRepository = recordRepository;
    }

    /**
     * {@code POST  /records} : Create a new record.
     *
     * @param recordDTO the recordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recordDTO, or with status {@code 400 (Bad Request)} if the record has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/records")
    public ResponseEntity<RecordDTO> createRecord(@Valid @RequestBody RecordDTO recordDTO) throws URISyntaxException {
        log.debug("REST request to save Record : {}", recordDTO);
        if (recordDTO.getId() != null) {
            throw new BadRequestAlertException("A new record cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecordDTO result = recordService.save(recordDTO);
        return ResponseEntity
            .created(new URI("/api/v1/records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /records/:id} : Updates an existing record.
     *
     * @param id the id of the recordDTO to save.
     * @param recordDTO the recordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recordDTO,
     * or with status {@code 400 (Bad Request)} if the recordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/records/{id}")
    public ResponseEntity<RecordDTO> updateRecord(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody RecordDTO recordDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Record : {}, {}", id, recordDTO);
        if (recordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RecordDTO result = recordService.update(recordDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recordDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /records/:id} : Partial updates given fields of an existing record, field will ignore if it is null
     *
     * @param id the id of the recordDTO to save.
     * @param recordDTO the recordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recordDTO,
     * or with status {@code 400 (Bad Request)} if the recordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the recordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the recordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/records/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecordDTO> partialUpdateRecord(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody RecordDTO recordDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Record partially : {}, {}", id, recordDTO);
        if (recordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecordDTO> result = recordService.partialUpdate(recordDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recordDTO.getId())
        );
    }

    /**
     * {@code GET  /records} : get all the records.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of records in body.
     */
    @GetMapping("/records")
    public ResponseEntity<List<RecordDTO>> getAllRecords(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Records");
        Page<RecordDTO> page = recordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /records/:id} : get the "id" record.
     *
     * @param id the id of the recordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/records/{id}")
    public ResponseEntity<RecordDTO> getRecord(@PathVariable String id) {
        log.debug("REST request to get Record : {}", id);
        Optional<RecordDTO> recordDTO = recordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recordDTO);
    }

    /**
     * {@code DELETE  /records/:id} : delete the "id" record.
     *
     * @param id the id of the recordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/records/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable String id) {
        log.debug("REST request to delete Record : {}", id);
        recordService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
