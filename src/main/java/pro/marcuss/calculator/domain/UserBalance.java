package pro.marcuss.calculator.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Indexed(unique = true)
    private String userLogin;

    @DBRef
    @Field("user")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

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

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserBalance user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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

    // prettier-ignore
    @Override
    public String toString() {
        return "UserBalance{" +
            "userLogin=" + getUserLogin() +
            ", balance=" + getBalance() +
            "}";
    }
}
