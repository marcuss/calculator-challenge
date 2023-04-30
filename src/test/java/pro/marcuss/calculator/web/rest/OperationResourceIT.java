package pro.marcuss.calculator.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import pro.marcuss.calculator.domain.Operation;
import pro.marcuss.calculator.domain.enumeration.Operator;
import pro.marcuss.calculator.repository.OperationRepository;
import pro.marcuss.calculator.service.dto.OperationDTO;
import pro.marcuss.calculator.service.mapper.OperationMapper;

/**
 * Integration tests for the {@link OperationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperationResourceIT {

    private static final Operator DEFAULT_OPERATOR = Operator.ADD;
    private static final Operator UPDATED_OPERATOR = Operator.SUBSTRACT;

    private static final Double DEFAULT_COST = 0D;
    private static final Double UPDATED_COST = 1D;

    private static final String ENTITY_API_URL = "/api/v1/operations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private MockMvc restOperationMockMvc;

    private Operation operation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operation createEntity() {
        Operation operation = new Operation().operator(DEFAULT_OPERATOR).cost(DEFAULT_COST);
        return operation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operation createUpdatedEntity() {
        Operation operation = new Operation().operator(UPDATED_OPERATOR).cost(UPDATED_COST);
        return operation;
    }

    @BeforeEach
    public void initTest() {
        operationRepository.deleteAll();
        operation = createEntity();
    }

    @Test
    void createOperation() throws Exception {
        int databaseSizeBeforeCreate = operationRepository.findAll().size();
        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);
        restOperationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isCreated());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeCreate + 1);
        Operation testOperation = operationList.get(operationList.size() - 1);
        assertThat(testOperation.getOperator()).isEqualTo(DEFAULT_OPERATOR);
        assertThat(testOperation.getCost()).isEqualTo(DEFAULT_COST);
    }

    @Test
    void createOperationWithExistingId() throws Exception {
        // Create the Operation with an existing ID
        operation.setId("existing_id");
        OperationDTO operationDTO = operationMapper.toDto(operation);

        int databaseSizeBeforeCreate = operationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkOperatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setOperator(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = operationRepository.findAll().size();
        // set the field null
        operation.setCost(null);

        // Create the Operation, which fails.
        OperationDTO operationDTO = operationMapper.toDto(operation);

        restOperationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isBadRequest());

        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllOperations() throws Exception {
        // Initialize the database
        operation.setId(UUID.randomUUID().toString());
        operationRepository.save(operation);

        // Get all the operationList
        restOperationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operation.getId())))
            .andExpect(jsonPath("$.[*].operator").value(hasItem(DEFAULT_OPERATOR.toString())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())));
    }

    @Test
    void getOperation() throws Exception {
        // Initialize the database
        operation.setId(UUID.randomUUID().toString());
        operationRepository.save(operation);

        // Get the operation
        restOperationMockMvc
            .perform(get(ENTITY_API_URL_ID, operation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operation.getId()))
            .andExpect(jsonPath("$.operator").value(DEFAULT_OPERATOR.toString()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()));
    }

    @Test
    void getNonExistingOperation() throws Exception {
        // Get the operation
        restOperationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingOperation() throws Exception {
        // Initialize the database
        operation.setId(UUID.randomUUID().toString());
        operationRepository.save(operation);

        int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Update the operation
        Operation updatedOperation = operationRepository.findById(operation.getId()).get();
        updatedOperation.operator(UPDATED_OPERATOR).cost(UPDATED_COST);
        OperationDTO operationDTO = operationMapper.toDto(updatedOperation);

        restOperationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
        Operation testOperation = operationList.get(operationList.size() - 1);
        assertThat(testOperation.getOperator()).isEqualTo(UPDATED_OPERATOR);
        assertThat(testOperation.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    void putNonExistingOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();
        operation.setId(UUID.randomUUID().toString());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();
        operation.setId(UUID.randomUUID().toString());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();
        operation.setId(UUID.randomUUID().toString());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOperationWithPatch() throws Exception {
        // Initialize the database
        operation.setId(UUID.randomUUID().toString());
        operationRepository.save(operation);

        int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Update the operation using partial update
        Operation partialUpdatedOperation = new Operation();
        partialUpdatedOperation.setId(operation.getId());

        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperation))
            )
            .andExpect(status().isOk());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
        Operation testOperation = operationList.get(operationList.size() - 1);
        assertThat(testOperation.getOperator()).isEqualTo(DEFAULT_OPERATOR);
        assertThat(testOperation.getCost()).isEqualTo(DEFAULT_COST);
    }

    @Test
    void fullUpdateOperationWithPatch() throws Exception {
        // Initialize the database
        operation.setId(UUID.randomUUID().toString());
        operationRepository.save(operation);

        int databaseSizeBeforeUpdate = operationRepository.findAll().size();

        // Update the operation using partial update
        Operation partialUpdatedOperation = new Operation();
        partialUpdatedOperation.setId(operation.getId());

        partialUpdatedOperation.operator(UPDATED_OPERATOR).cost(UPDATED_COST);

        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperation))
            )
            .andExpect(status().isOk());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
        Operation testOperation = operationList.get(operationList.size() - 1);
        assertThat(testOperation.getOperator()).isEqualTo(UPDATED_OPERATOR);
        assertThat(testOperation.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    void patchNonExistingOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();
        operation.setId(UUID.randomUUID().toString());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();
        operation.setId(UUID.randomUUID().toString());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOperation() throws Exception {
        int databaseSizeBeforeUpdate = operationRepository.findAll().size();
        operation.setId(UUID.randomUUID().toString());

        // Create the Operation
        OperationDTO operationDTO = operationMapper.toDto(operation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operation in the database
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOperation() throws Exception {
        // Initialize the database
        operation.setId(UUID.randomUUID().toString());
        operationRepository.save(operation);

        int databaseSizeBeforeDelete = operationRepository.findAll().size();

        // Delete the operation
        restOperationMockMvc
            .perform(delete(ENTITY_API_URL_ID, operation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Operation> operationList = operationRepository.findAll();
        assertThat(operationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
