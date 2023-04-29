package pro.marcuss.calculator.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pro.marcuss.calculator.web.rest.TestUtil;

class UserBalanceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserBalanceDTO.class);
        UserBalanceDTO userBalanceDTO1 = new UserBalanceDTO();
        userBalanceDTO1.setId("id1");
        UserBalanceDTO userBalanceDTO2 = new UserBalanceDTO();
        assertThat(userBalanceDTO1).isNotEqualTo(userBalanceDTO2);
        userBalanceDTO2.setId(userBalanceDTO1.getId());
        assertThat(userBalanceDTO1).isEqualTo(userBalanceDTO2);
        userBalanceDTO2.setId("id2");
        assertThat(userBalanceDTO1).isNotEqualTo(userBalanceDTO2);
        userBalanceDTO1.setId(null);
        assertThat(userBalanceDTO1).isNotEqualTo(userBalanceDTO2);
    }
}
