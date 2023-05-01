package pro.marcuss.calculator.config.dbmigrations;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import pro.marcuss.calculator.config.Constants;
import pro.marcuss.calculator.domain.Authority;
import pro.marcuss.calculator.domain.Operation;
import pro.marcuss.calculator.domain.User;
import pro.marcuss.calculator.domain.UserBalance;
import pro.marcuss.calculator.domain.enumeration.Operator;
import pro.marcuss.calculator.security.AuthoritiesConstants;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Creates the initial database setup.
 */
@ChangeUnit(id = "users-initialization", order = "001")
public class InitialSetupMigration {

    private final MongoTemplate template;

    public InitialSetupMigration(MongoTemplate template) {
        this.template = template;
    }

    @Execution
    public void changeSet() {
        //programatically enforcing the creation fo unique indexes
        template.indexOps("operation").
            ensureIndex(new Index("operator", Sort.Direction.ASC).unique());
        template.indexOps("userBalance").
            ensureIndex(new Index("user_login", Sort.Direction.ASC).unique());
        Authority userAuthority = createUserAuthority();
        userAuthority = template.save(userAuthority);
        Authority adminAuthority = createAdminAuthority();
        adminAuthority = template.save(adminAuthority);
        addUsers(userAuthority, adminAuthority);
        createOperations().forEach(e -> template.save(e));
    }

    @RollbackExecution
    public void rollback() {}

    private Authority createAuthority(String authority) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(authority);
        return adminAuthority;
    }

    private Authority createAdminAuthority() {
        Authority adminAuthority = createAuthority(AuthoritiesConstants.ADMIN);
        return adminAuthority;
    }

    private Authority createUserAuthority() {
        Authority userAuthority = createAuthority(AuthoritiesConstants.USER);
        return userAuthority;
    }

    private void addUsers(Authority userAuthority, Authority adminAuthority) {
        User user = createUser(userAuthority);
        user = template.save(user);
        UserBalance userBalance = new UserBalance();
        userBalance.setUserLogin(user.getLogin());
        userBalance.setUser(user);
        userBalance.setBalance(Constants.DEFAULT_INITIAL_BALANCE);
        template.save(userBalance);
        User admin = createAdmin(adminAuthority, userAuthority);
        admin = template.save(admin);
        UserBalance userBalanceAdmin = new UserBalance();
        userBalanceAdmin.setUserLogin(admin.getLogin());
        userBalanceAdmin.setUser(admin);
        userBalanceAdmin.setBalance(1_000_000d);
        template.save(userBalanceAdmin);
    }

    private User createUser(Authority userAuthority) {
        User userUser = new User();
        userUser.setId("user-2");
        userUser.setLogin("user");
        userUser.setPassword("$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K");
        userUser.setFirstName("");
        userUser.setLastName("User");
        userUser.setEmail("user@localhost");
        userUser.setActivated(true);
        userUser.setLangKey("en");
        userUser.setCreatedBy(Constants.SYSTEM);
        userUser.setCreatedDate(Instant.now());
        userUser.getAuthorities().add(userAuthority);
        return userUser;
    }

    private User createAdmin(Authority adminAuthority, Authority userAuthority) {
        User adminUser = new User();
        adminUser.setId("user-1");
        adminUser.setLogin("admin");
        adminUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        adminUser.setFirstName("admin");
        adminUser.setLastName("Administrator");
        adminUser.setEmail("admin@localhost");
        adminUser.setActivated(true);
        adminUser.setLangKey("en");
        adminUser.setCreatedBy(Constants.SYSTEM);
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        return adminUser;
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
}
