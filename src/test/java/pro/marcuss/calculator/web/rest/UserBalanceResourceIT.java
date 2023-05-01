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
import pro.marcuss.calculator.domain.UserBalance;
import pro.marcuss.calculator.repository.UserBalanceRepository;
import pro.marcuss.calculator.service.dto.UserBalanceDTO;
import pro.marcuss.calculator.service.mapper.UserBalanceMapper;

/**
 * Integration tests for the {@link UserBalanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserBalanceResourceIT extends AbstractIntegrationTest {

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/v1/user-balances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Autowired
    private UserBalanceMapper userBalanceMapper;

    @Autowired
    private MockMvc restUserBalanceMockMvc;

    private UserBalance userBalance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserBalance createEntity() {
        UserBalance userBalance = new UserBalance().balance(DEFAULT_BALANCE).userLogin(DEFAULT_USER_LOGIN);
        return userBalance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserBalance createUpdatedEntity() {
        UserBalance userBalance = new UserBalance().balance(UPDATED_BALANCE).userLogin(UPDATED_USER_LOGIN);
        return userBalance;
    }

    @BeforeEach
    public void initTest() {
        userBalanceRepository.deleteAll();
        userBalance = createEntity();
        setUserBalanceForTests("user");
    }

    @Test
    void createUserBalance() throws Exception {
        int databaseSizeBeforeCreate = userBalanceRepository.findAll().size();
        // Create the UserBalance
        UserBalanceDTO userBalanceDTO = userBalanceMapper.toDto(userBalance);
        restUserBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userBalanceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserBalance in the database
        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        UserBalance testUserBalance = userBalanceList.get(userBalanceList.size() - 1);
        assertThat(testUserBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testUserBalance.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    void createUserBalanceWithExistingId() throws Exception {
        // Create the UserBalance with an existing ID
        userBalance.setId("existing_id");
        UserBalanceDTO userBalanceDTO = userBalanceMapper.toDto(userBalance);

        int databaseSizeBeforeCreate = userBalanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserBalance in the database
        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = userBalanceRepository.findAll().size();
        // set the field null
        userBalance.setBalance(null);

        // Create the UserBalance, which fails.
        UserBalanceDTO userBalanceDTO = userBalanceMapper.toDto(userBalance);

        restUserBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUserLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = userBalanceRepository.findAll().size();
        // set the field null
        userBalance.setUserLogin(null);

        // Create the UserBalance, which fails.
        UserBalanceDTO userBalanceDTO = userBalanceMapper.toDto(userBalance);

        restUserBalanceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllUserBalances() throws Exception {
        // Initialize the database
        userBalanceRepository.save(userBalance);

        // Get all the userBalanceList
        restUserBalanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userBalance.getId())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)));
    }

    @Test
    void getUserBalance() throws Exception {
        // Initialize the database
        userBalanceRepository.save(userBalance);

        // Get the userBalance
        restUserBalanceMockMvc
            .perform(get(ENTITY_API_URL_ID, userBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userBalance.getId()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN));
    }

    @Test
    void getNonExistingUserBalance() throws Exception {
        // Get the userBalance
        restUserBalanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingUserBalance() throws Exception {
        // Initialize the database
        userBalanceRepository.save(userBalance);

        int databaseSizeBeforeUpdate = userBalanceRepository.findAll().size();

        // Update the userBalance
        UserBalance updatedUserBalance = userBalanceRepository.findById(userBalance.getId()).get();
        updatedUserBalance.balance(UPDATED_BALANCE).userLogin(UPDATED_USER_LOGIN);
        UserBalanceDTO userBalanceDTO = userBalanceMapper.toDto(updatedUserBalance);

        restUserBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userBalanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userBalanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserBalance in the database
        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeUpdate);
        UserBalance testUserBalance = userBalanceList.get(userBalanceList.size() - 1);
        assertThat(testUserBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testUserBalance.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    void putNonExistingUserBalance() throws Exception {
        int databaseSizeBeforeUpdate = userBalanceRepository.findAll().size();
        userBalance.setId(UUID.randomUUID().toString());

        // Create the UserBalance
        UserBalanceDTO userBalanceDTO = userBalanceMapper.toDto(userBalance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userBalanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserBalance in the database
        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchUserBalance() throws Exception {
        int databaseSizeBeforeUpdate = userBalanceRepository.findAll().size();
        userBalance.setId(UUID.randomUUID().toString());

        // Create the UserBalance
        UserBalanceDTO userBalanceDTO = userBalanceMapper.toDto(userBalance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserBalanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserBalance in the database
        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamUserBalance() throws Exception {
        int databaseSizeBeforeUpdate = userBalanceRepository.findAll().size();
        userBalance.setId(UUID.randomUUID().toString());

        // Create the UserBalance
        UserBalanceDTO userBalanceDTO = userBalanceMapper.toDto(userBalance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserBalanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userBalanceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserBalance in the database
        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateUserBalanceWithPatch() throws Exception {
        // Initialize the database
        userBalanceRepository.save(userBalance);

        int databaseSizeBeforeUpdate = userBalanceRepository.findAll().size();

        // Update the userBalance using partial update
        UserBalance partialUpdatedUserBalance = new UserBalance();
        partialUpdatedUserBalance.setId(userBalance.getId());

        restUserBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserBalance))
            )
            .andExpect(status().isOk());

        // Validate the UserBalance in the database
        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeUpdate);
        UserBalance testUserBalance = userBalanceList.get(userBalanceList.size() - 1);
        assertThat(testUserBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testUserBalance.getUserLogin()).isEqualTo(DEFAULT_USER_LOGIN);
    }

    @Test
    void fullUpdateUserBalanceWithPatch() throws Exception {
        // Initialize the database
        userBalanceRepository.save(userBalance);

        int databaseSizeBeforeUpdate = userBalanceRepository.findAll().size();

        // Update the userBalance using partial update
        UserBalance partialUpdatedUserBalance = new UserBalance();
        partialUpdatedUserBalance.setId(userBalance.getId());

        partialUpdatedUserBalance.balance(UPDATED_BALANCE).userLogin(UPDATED_USER_LOGIN);

        restUserBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserBalance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserBalance))
            )
            .andExpect(status().isOk());

        // Validate the UserBalance in the database
        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeUpdate);
        UserBalance testUserBalance = userBalanceList.get(userBalanceList.size() - 1);
        assertThat(testUserBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testUserBalance.getUserLogin()).isEqualTo(UPDATED_USER_LOGIN);
    }

    @Test
    void patchNonExistingUserBalance() throws Exception {
        int databaseSizeBeforeUpdate = userBalanceRepository.findAll().size();
        userBalance.setId(UUID.randomUUID().toString());

        // Create the UserBalance
        UserBalanceDTO userBalanceDTO = userBalanceMapper.toDto(userBalance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userBalanceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserBalance in the database
        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchUserBalance() throws Exception {
        int databaseSizeBeforeUpdate = userBalanceRepository.findAll().size();
        userBalance.setId(UUID.randomUUID().toString());

        // Create the UserBalance
        UserBalanceDTO userBalanceDTO = userBalanceMapper.toDto(userBalance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userBalanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserBalance in the database
        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamUserBalance() throws Exception {
        int databaseSizeBeforeUpdate = userBalanceRepository.findAll().size();
        userBalance.setId(UUID.randomUUID().toString());

        // Create the UserBalance
        UserBalanceDTO userBalanceDTO = userBalanceMapper.toDto(userBalance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserBalanceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userBalanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserBalance in the database
        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteUserBalance() throws Exception {
        // Initialize the database
        userBalanceRepository.save(userBalance);

        int databaseSizeBeforeDelete = userBalanceRepository.findAll().size();

        // Delete the userBalance
        restUserBalanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, userBalance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserBalance> userBalanceList = userBalanceRepository.findAll();
        assertThat(userBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
