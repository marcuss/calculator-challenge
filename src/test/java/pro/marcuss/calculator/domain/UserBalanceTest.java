package pro.marcuss.calculator.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pro.marcuss.calculator.web.rest.TestUtil;

class UserBalanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserBalance.class);
        UserBalance userBalance1 = new UserBalance();
        userBalance1.setId("id1");
        UserBalance userBalance2 = new UserBalance();
        userBalance2.setId(userBalance1.getId());
        assertThat(userBalance1).isEqualTo(userBalance2);
        userBalance2.setId("id2");
        assertThat(userBalance1).isNotEqualTo(userBalance2);
        userBalance1.setId(null);
        assertThat(userBalance1).isNotEqualTo(userBalance2);
    }
}
