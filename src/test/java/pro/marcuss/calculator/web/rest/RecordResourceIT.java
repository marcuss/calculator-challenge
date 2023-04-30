package pro.marcuss.calculator.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pro.marcuss.calculator.IntegrationTest;
import pro.marcuss.calculator.domain.Record;
import pro.marcuss.calculator.domain.enumeration.Operator;
import pro.marcuss.calculator.repository.RecordRepository;
import pro.marcuss.calculator.service.dto.RecordDTO;
import pro.marcuss.calculator.service.mapper.RecordMapper;

/**
 * Integration tests for the {@link RecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecordResourceIT {

    private static final Boolean DEFAULT_ACTIVE = true;
    private static final Boolean UPDATED_ACTIVE = false;

    private static final Operator DEFAULT_OPERATION_ID = Operator.ADD;
    private static final Operator UPDATED_OPERATION_ID = Operator.SUBSTRACT;

    private static final Double DEFAULT_AMOUNT = 0D;
    private static final Double UPDATED_AMOUNT = 1D;

    private static final Double DEFAULT_USER_BALANCE = 1D;
    private static final Double UPDATED_USER_BALANCE = 2D;

    private static final String DEFAULT_OPERATION_RESPOSE = "AAAAAAAAAA";
    private static final String UPDATED_OPERATION_RESPOSE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/v1/records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private MockMvc restRecordMockMvc;

    private Record record;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Record createEntity() {
        Record record = new Record()
            .active(DEFAULT_ACTIVE)
            .operationId(DEFAULT_OPERATION_ID)
            .amount(DEFAULT_AMOUNT)
            .userBalance(DEFAULT_USER_BALANCE)
            .operationRespose(DEFAULT_OPERATION_RESPOSE)
            .date(DEFAULT_DATE);
        return record;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Record createUpdatedEntity() {
        Record record = new Record()
            .active(UPDATED_ACTIVE)
            .operationId(UPDATED_OPERATION_ID)
            .amount(UPDATED_AMOUNT)
            .userBalance(UPDATED_USER_BALANCE)
            .operationRespose(UPDATED_OPERATION_RESPOSE)
            .date(UPDATED_DATE);
        return record;
    }

    @BeforeEach
    public void initTest() {
        recordRepository.deleteAll();
        record = createEntity();
    }

    @Test
    void createRecord() throws Exception {
        int databaseSizeBeforeCreate = recordRepository.findAll().size();
        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);
        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isCreated());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeCreate + 1);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testRecord.getOperationId()).isEqualTo(DEFAULT_OPERATION_ID);
        assertThat(testRecord.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testRecord.getUserBalance()).isEqualTo(DEFAULT_USER_BALANCE);
        assertThat(testRecord.getOperationRespose()).isEqualTo(DEFAULT_OPERATION_RESPOSE);
        assertThat(testRecord.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    void createRecordWithExistingId() throws Exception {
        // Create the Record with an existing ID
        record.setId("existing_id");
        RecordDTO recordDTO = recordMapper.toDto(record);

        int databaseSizeBeforeCreate = recordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setActive(null);

        // Create the Record, which fails.
        RecordDTO recordDTO = recordMapper.toDto(record);

        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isBadRequest());

        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setAmount(null);

        // Create the Record, which fails.
        RecordDTO recordDTO = recordMapper.toDto(record);

        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isBadRequest());

        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUserBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setUserBalance(null);

        // Create the Record, which fails.
        RecordDTO recordDTO = recordMapper.toDto(record);

        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isBadRequest());

        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkOperationResposeIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setOperationRespose(null);

        // Create the Record, which fails.
        RecordDTO recordDTO = recordMapper.toDto(record);

        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isBadRequest());

        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllRecords() throws Exception {
        // Initialize the database
        record.setId(UUID.randomUUID().toString());
        recordRepository.save(record);

        // Get all the recordList
        restRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(record.getId())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].operationId").value(hasItem(DEFAULT_OPERATION_ID.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].userBalance").value(hasItem(DEFAULT_USER_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].operationRespose").value(hasItem(DEFAULT_OPERATION_RESPOSE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    void getRecord() throws Exception {
        // Initialize the database
        record.setId(UUID.randomUUID().toString());
        recordRepository.save(record);

        // Get the record
        restRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, record.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(record.getId()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.operationId").value(DEFAULT_OPERATION_ID.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.userBalance").value(DEFAULT_USER_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.operationRespose").value(DEFAULT_OPERATION_RESPOSE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    void getNonExistingRecord() throws Exception {
        // Get the record
        restRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingRecord() throws Exception {
        // Initialize the database
        record.setId(UUID.randomUUID().toString());
        recordRepository.save(record);

        int databaseSizeBeforeUpdate = recordRepository.findAll().size();

        // Update the record
        Record updatedRecord = recordRepository.findById(record.getId()).get();
        updatedRecord
            .active(UPDATED_ACTIVE)
            .operationId(UPDATED_OPERATION_ID)
            .amount(UPDATED_AMOUNT)
            .userBalance(UPDATED_USER_BALANCE)
            .operationRespose(UPDATED_OPERATION_RESPOSE)
            .date(UPDATED_DATE);
        RecordDTO recordDTO = recordMapper.toDto(updatedRecord);

        restRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recordDTO))
            )
            .andExpect(status().isOk());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testRecord.getOperationId()).isEqualTo(UPDATED_OPERATION_ID);
        assertThat(testRecord.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testRecord.getUserBalance()).isEqualTo(UPDATED_USER_BALANCE);
        assertThat(testRecord.getOperationRespose()).isEqualTo(UPDATED_OPERATION_RESPOSE);
        assertThat(testRecord.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    void putNonExistingRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.setId(UUID.randomUUID().toString());

        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.setId(UUID.randomUUID().toString());

        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.setId(UUID.randomUUID().toString());

        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRecordWithPatch() throws Exception {
        // Initialize the database
        record.setId(UUID.randomUUID().toString());
        recordRepository.save(record);

        int databaseSizeBeforeUpdate = recordRepository.findAll().size();

        // Update the record using partial update
        Record partialUpdatedRecord = new Record();
        partialUpdatedRecord.setId(record.getId());

        partialUpdatedRecord.userBalance(UPDATED_USER_BALANCE);

        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecord))
            )
            .andExpect(status().isOk());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testRecord.getOperationId()).isEqualTo(DEFAULT_OPERATION_ID);
        assertThat(testRecord.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testRecord.getUserBalance()).isEqualTo(UPDATED_USER_BALANCE);
        assertThat(testRecord.getOperationRespose()).isEqualTo(DEFAULT_OPERATION_RESPOSE);
        assertThat(testRecord.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    void fullUpdateRecordWithPatch() throws Exception {
        // Initialize the database
        record.setId(UUID.randomUUID().toString());
        recordRepository.save(record);

        int databaseSizeBeforeUpdate = recordRepository.findAll().size();

        // Update the record using partial update
        Record partialUpdatedRecord = new Record();
        partialUpdatedRecord.setId(record.getId());

        partialUpdatedRecord
            .active(UPDATED_ACTIVE)
            .operationId(UPDATED_OPERATION_ID)
            .amount(UPDATED_AMOUNT)
            .userBalance(UPDATED_USER_BALANCE)
            .operationRespose(UPDATED_OPERATION_RESPOSE)
            .date(UPDATED_DATE);

        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecord))
            )
            .andExpect(status().isOk());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
        Record testRecord = recordList.get(recordList.size() - 1);
        assertThat(testRecord.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testRecord.getOperationId()).isEqualTo(UPDATED_OPERATION_ID);
        assertThat(testRecord.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testRecord.getUserBalance()).isEqualTo(UPDATED_USER_BALANCE);
        assertThat(testRecord.getOperationRespose()).isEqualTo(UPDATED_OPERATION_RESPOSE);
        assertThat(testRecord.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    void patchNonExistingRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.setId(UUID.randomUUID().toString());

        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.setId(UUID.randomUUID().toString());

        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRecord() throws Exception {
        int databaseSizeBeforeUpdate = recordRepository.findAll().size();
        record.setId(UUID.randomUUID().toString());

        // Create the Record
        RecordDTO recordDTO = recordMapper.toDto(record);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecordMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(recordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Record in the database
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRecord() throws Exception {
        // Initialize the database
        record.setId(UUID.randomUUID().toString());
        recordRepository.save(record);

        int databaseSizeBeforeDelete = recordRepository.findAll().size();

        // Delete the record
        restRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, record.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
