package pro.marcuss.calculator.web.rest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import pro.marcuss.calculator.domain.Operation;
import pro.marcuss.calculator.domain.User;
import pro.marcuss.calculator.domain.enumeration.Operator;
import pro.marcuss.calculator.repository.OperationRepository;
import pro.marcuss.calculator.repository.UserRepository;
import pro.marcuss.calculator.service.UserBalanceService;
import pro.marcuss.calculator.service.dto.UserBalanceDTO;
import pro.marcuss.calculator.service.dto.UserDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static pro.marcuss.calculator.config.Constants.DEFAULT_INITIAL_BALANCE;

public class UserBalanceIntegrationTest {

    @Autowired
    protected UserBalanceService userBalanceService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected OperationRepository operationRepository;


    protected void setUserBalanceForTests(String login) {
        Optional<UserBalanceDTO> userBalanceDTO = userBalanceService.findUserBalanceByUserLogin(login);
        if (userBalanceDTO.isEmpty()) {
            UserBalanceDTO newUserBalanceDTO = new UserBalanceDTO();
            newUserBalanceDTO.setBalance(DEFAULT_INITIAL_BALANCE);
            newUserBalanceDTO.setUserLogin(login);
            userBalanceService.save(newUserBalanceDTO);
        } else {
            userBalanceDTO.get().setBalance(DEFAULT_INITIAL_BALANCE);
            userBalanceService.update(userBalanceDTO.get());
        }
    }

    protected void setUserBalanceForTests(User user) {
        Optional<UserBalanceDTO> userBalanceDTO = userBalanceService.findUserBalanceByUserLogin(user.getLogin());
        if (userBalanceDTO.isEmpty()) {
            UserBalanceDTO newUserBalanceDTO = new UserBalanceDTO();
            newUserBalanceDTO.setBalance(DEFAULT_INITIAL_BALANCE);
            newUserBalanceDTO.setUserLogin(user.getLogin());
            newUserBalanceDTO.setUser(Optional.of(user).map(UserDTO::new).get());
            userBalanceService.save(newUserBalanceDTO);
        } else {
            userBalanceDTO.get().setBalance(DEFAULT_INITIAL_BALANCE);
            userBalanceDTO.get().setUser(Optional.of(user).map(UserDTO::new).get());
            userBalanceService.update(userBalanceDTO.get());
        }
    }

    protected User setUserForTest(String login) {
        User user = new User();
        user.setLogin(login);
        user.setEmail(login + "@example.com");
        user.setPassword(RandomStringUtils.randomAlphanumeric(60));
        user.setActivated(true);
        return userRepository.save(user);
    }

    protected User setUserTests(User user) {
        return userRepository.save(user);
    }

    private List<Operation> createOperations() {
        return Arrays.stream(Operator.values()).map(
            operator -> new Operation()
                .operator(operator)
                .cost(createRandomDouble(3))
        ).collect(Collectors.toList());
    }

    private double createRandomDouble(int decimalPlaces) {
        double scale = Math.pow(10, decimalPlaces);
        return Math.round((0.9 + 9 * new Random().nextDouble()) * scale) / scale;
    }


    protected void setOperationCosts() {
        operationRepository.deleteAll();
        createOperations().forEach(e -> operationRepository.save(e));
    }
}
