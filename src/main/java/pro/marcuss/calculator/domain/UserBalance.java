package pro.marcuss.calculator.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A UserBalance.
 */
@Document(collection = "user_balance")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("balance")
    private Double balance;

    @NotNull
    @Field("user_login")
    private String userLogin;

    public String getId() {
        return this.id;
    }

    public UserBalance id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getBalance() {
        return this.balance;
    }

    public UserBalance balance(Double balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public UserBalance userLogin(String userLogin) {
        this.setUserLogin(userLogin);
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserBalance)) {
            return false;
        }
        return id != null && id.equals(((UserBalance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UserBalance{" +
            "userLogin=" + getUserLogin() +
            ", balance=" + getBalance() +
            "}";
    }
}
