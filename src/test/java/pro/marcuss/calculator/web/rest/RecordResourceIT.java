package pro.marcuss.calculator.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pro.marcuss.calculator.IntegrationTest;
import pro.marcuss.calculator.config.Constants;
import pro.marcuss.calculator.domain.Record;
import pro.marcuss.calculator.domain.enumeration.Operator;
import pro.marcuss.calculator.repository.RecordRepository;
import pro.marcuss.calculator.service.dto.RecordDTO;
import pro.marcuss.calculator.service.mapper.RecordMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecordResourceIT extends UserBalanceIntegrationTest {

    private static final String DEFAULT_USER_LOGIN = "user";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = true;
    private static final Boolean UPDATED_ACTIVE = false;

    private static final Operator DEFAULT_OPERATION = Operator.ADD;
    private static final Operator UPDATED_OPERATION = Operator.SUBSTRACT;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Double DEFAULT_USER_BALANCE = 1D;
    private static final Double UPDATED_USER_BALANCE = 2D;

    private static final String DEFAULT_OPERATION_RESPONSE = "AAAAAAAAAA";
    private static final String UPDATED_OPERATION_RESPONSE = "BBBBBBBBBB";

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
            .userLogin(DEFAULT_USER_LOGIN)
            .active(DEFAULT_ACTIVE)
            .operation(DEFAULT_OPERATION)
            .amount(DEFAULT_AMOUNT)
            .userBalance(DEFAULT_USER_BALANCE)
            .operationResponse(DEFAULT_OPERATION_RESPONSE)
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
            .userLogin(UPDATED_USER_LOGIN)
            .active(UPDATED_ACTIVE)
            .operation(UPDATED_OPERATION)
            .amount(UPDATED_AMOUNT)
            .userBalance(UPDATED_USER_BALANCE)
            .operationResponse(UPDATED_OPERATION_RESPONSE)
            .date(UPDATED_DATE);
        return record;
    }

    @BeforeEach
    public void initTest() {
        recordRepository.deleteAll();
        record = createEntity();
        recordRepository.deleteAll();
        userRepository.deleteAll();
        setUserBalanceForTests(setUserForTest("user"));
        setOperationCosts();
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
        assertThat(testRecord.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testRecord.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testRecord.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testRecord.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testRecord.getUserBalance()).isLessThan(Constants.DEFAULT_INITIAL_BALANCE);//Balance was deducted
        assertThat(testRecord.getOperationResponse()).isNotBlank();
        assertThat(testRecord.getDate()).isAfter(Instant.now().minus(1, ChronoUnit.HOURS));
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
    void checkUserLoginIsNotRequired() throws Exception {
        setUserBalanceForTests("user");
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setUserLogin(null);

        // Create the Record, which fails.
        RecordDTO recordDTO = recordMapper.toDto(record);

        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isCreated());

        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeTest +1);
    }

    @Test
    void checkActiveIsNotRequired() throws Exception { //the field is set to true in case is missing.
           setUserBalanceForTests("user");
            int databaseSizeBeforeTest = recordRepository.findAll().size();
            // set the field null
            record.setActive(null);

            // Create the Record, which fails.
            RecordDTO recordDTO = recordMapper.toDto(record);

            restRecordMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
                .andExpect(status().isCreated());

            List<Record> recordList = recordRepository.findAll();
            assertThat(recordList).hasSize(databaseSizeBeforeTest + 1);
        }

    @Test
    void checkOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setOperation(null);

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
    void checkUserBalanceIsNotRequired() throws Exception { //field is set to 0 or last persisted userBalance.balance for user
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setUserBalance(null);

        // Create the Record, which fails.
        RecordDTO recordDTO = recordMapper.toDto(record);

        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isCreated());

        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeTest + 1);
    }

    @Test
    void checkOperationResponseIsNotRequired() throws Exception {//field is calculated during persistence
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setOperationResponse(null);

        // Create the Record, which fails.
        RecordDTO recordDTO = recordMapper.toDto(record);

        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isCreated());

        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeTest + 1);
    }

    @Test
    void checkDateIsNotRequired() throws Exception { //field is set to now() in case is missing.
        int databaseSizeBeforeTest = recordRepository.findAll().size();
        // set the field null
        record.setDate(null);

        // Create the Record, which fails.
        RecordDTO recordDTO = recordMapper.toDto(record);

        restRecordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recordDTO)))
            .andExpect(status().isCreated());

        List<Record> recordList = recordRepository.findAll();
        assertThat(recordList).hasSize(databaseSizeBeforeTest + 1);
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
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].userBalance").value(hasItem(DEFAULT_USER_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].operationResponse").value(hasItem(DEFAULT_OPERATION_RESPONSE)))
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
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.userBalance").value(DEFAULT_USER_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.operationResponse").value(DEFAULT_OPERATION_RESPONSE))
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
            .userLogin(UPDATED_USER_LOGIN)
            .active(UPDATED_ACTIVE)
            .operation(UPDATED_OPERATION)
            .amount(UPDATED_AMOUNT)
            .userBalance(UPDATED_USER_BALANCE)
            .operationResponse(UPDATED_OPERATION_RESPONSE)
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
        assertThat(testRecord.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testRecord.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testRecord.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testRecord.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testRecord.getUserBalance()).isEqualTo(UPDATED_USER_BALANCE);
        assertThat(testRecord.getOperationResponse()).isEqualTo(UPDATED_OPERATION_RESPONSE);
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
        setUserForTest("user");
        setUserBalanceForTests("user");

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
        assertThat(testRecord.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
        assertThat(testRecord.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testRecord.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testRecord.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testRecord.getUserBalance()).isEqualTo(UPDATED_USER_BALANCE);
        assertThat(testRecord.getOperationResponse()).isEqualTo(DEFAULT_OPERATION_RESPONSE);
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
            .userLogin(UPDATED_USER_LOGIN)
            .active(UPDATED_ACTIVE)
            .operation(UPDATED_OPERATION)
            .amount(UPDATED_AMOUNT)
            .userBalance(UPDATED_USER_BALANCE)
            .operationResponse(UPDATED_OPERATION_RESPONSE)
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
        assertThat(testRecord.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
        assertThat(testRecord.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testRecord.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testRecord.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testRecord.getUserBalance()).isEqualTo(UPDATED_USER_BALANCE);
        assertThat(testRecord.getOperationResponse()).isEqualTo(UPDATED_OPERATION_RESPONSE);
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
    void softDeleteRecordOnly() throws Exception {
        // Initialize the database
        record.setId(UUID.randomUUID().toString());
        recordRepository.save(record);

        int recordsBeforeDelete = recordRepository.findAll().size();
        int activeRecordsBeforeDelete = recordRepository.findAllByActiveIsTrueOrderByDateDesc().size();

        // Delete the record
        restRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, record.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Record> recordList = recordRepository.findAll();
        activeRecordsBeforeDelete = recordRepository.findAllByActiveIsTrueOrderByDateDesc().size();
        assertThat(recordList).hasSize(recordsBeforeDelete);
        assertThat(recordList).hasSize(activeRecordsBeforeDelete + 1);

    }
}
