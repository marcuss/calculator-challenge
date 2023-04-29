package pro.marcuss.calculator.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserBalanceMapperTest {

    private UserBalanceMapper userBalanceMapper;

    @BeforeEach
    public void setUp() {
        userBalanceMapper = new UserBalanceMapperImpl();
    }
}
