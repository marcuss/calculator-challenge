package pro.marcuss.calculator.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pro.marcuss.calculator.web.rest.TestUtil;

class OperationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperationDTO.class);
        OperationDTO operationDTO1 = new OperationDTO();
        operationDTO1.setId("id1");
        OperationDTO operationDTO2 = new OperationDTO();
        assertThat(operationDTO1).isNotEqualTo(operationDTO2);
        operationDTO2.setId(operationDTO1.getId());
        assertThat(operationDTO1).isEqualTo(operationDTO2);
        operationDTO2.setId("id2");
        assertThat(operationDTO1).isNotEqualTo(operationDTO2);
        operationDTO1.setId(null);
        assertThat(operationDTO1).isNotEqualTo(operationDTO2);
    }
}
