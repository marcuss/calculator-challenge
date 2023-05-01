package pro.marcuss.calculator.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pro.marcuss.calculator.domain.enumeration.Operator;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Record.
 */
@Document(collection = "record")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("user_login")
    private String userLogin;

    @NotNull
    @Field("active")
    private Boolean active;

    @NotNull
    @Field("operation")
    private Operator operation;

    @NotNull
    @Field("amount")
    private Double amount;

    @NotNull
    @Field("user_balance")
    private Double userBalance;

    @NotNull
    @Field("operation_response")
    private String operationResponse;

    @NotNull
    @Field("date")
    private Instant date;

    @DBRef
    @Field("user")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Record id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public Record userLogin(String userLogin) {
        this.setUserLogin(userLogin);
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Record active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Operator getOperation() {
        return this.operation;
    }

    public Record operation(Operator operation) {
        this.setOperation(operation);
        return this;
    }

    public void setOperation(Operator operation) {
        this.operation = operation;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Record amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getUserBalance() {
        return this.userBalance;
    }

    public Record userBalance(Double userBalance) {
        this.setUserBalance(userBalance);
        return this;
    }

    public void setUserBalance(Double userBalance) {
        this.userBalance = userBalance;
    }

    public String getOperationResponse() {
        return this.operationResponse;
    }

    public Record operationResponse(String operationResponse) {
        this.setOperationResponse(operationResponse);
        return this;
    }

    public void setOperationResponse(String operationResponse) {
        this.operationResponse = operationResponse;
    }

    public Instant getDate() {
        return this.date;
    }

    public Record date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Record user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Record)) {
            return false;
        }
        return id != null && id.equals(((Record) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Record{" +
            "id=" + getId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", active='" + getActive() + "'" +
            ", operation='" + getOperation() + "'" +
            ", amount=" + getAmount() +
            ", userBalance=" + getUserBalance() +
            ", operationResponse='" + getOperationResponse() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
