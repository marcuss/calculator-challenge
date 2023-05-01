package pro.marcuss.calculator.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link pro.marcuss.calculator.domain.UserBalance} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserBalanceDTO implements Serializable {

    private String id;

    @NotNull
    private Double balance;

    @NotNull
    private String userLogin;

    private UserDTO user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserBalanceDTO)) {
            return false;
        }

        UserBalanceDTO userBalanceDTO = (UserBalanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userBalanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserBalanceDTO{" +
            "id='" + getId() + "'" +
            ", balance=" + getBalance() +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
