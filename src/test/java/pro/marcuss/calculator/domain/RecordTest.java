package pro.marcuss.calculator.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pro.marcuss.calculator.web.rest.TestUtil;

class RecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Record.class);
        Record record1 = new Record();
        record1.setId("id1");
        Record record2 = new Record();
        record2.setId(record1.getId());
        assertThat(record1).isEqualTo(record2);
        record2.setId("id2");
        assertThat(record1).isNotEqualTo(record2);
        record1.setId(null);
        assertThat(record1).isNotEqualTo(record2);
    }
}
