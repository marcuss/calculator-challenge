package pro.marcuss.calculator.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pro.marcuss.calculator.web.rest.TestUtil;

class OperationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operation.class);
        Operation operation1 = new Operation();
        operation1.setId("id1");
        Operation operation2 = new Operation();
        operation2.setId(operation1.getId());
        assertThat(operation1).isEqualTo(operation2);
        operation2.setId("id2");
        assertThat(operation1).isNotEqualTo(operation2);
        operation1.setId(null);
        assertThat(operation1).isNotEqualTo(operation2);
    }
}
