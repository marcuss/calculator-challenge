package pro.marcuss.calculator.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pro.marcuss.calculator.domain.enumeration.Operator;

/**
 * A Record.
 */
@Document(collection = "record")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("operation_id")
    private Operator operationId;

    @NotNull
    @DecimalMin(value = "0")
    @Field("amount")
    private Double amount;

    @NotNull
    @Field("user_balance")
    private Double userBalance;

    @NotNull
    @Field("operation_respose")
    private String operationRespose;

    @Field("date")
    private Instant date;

    @DBRef
    @Field("userId")
    private User userId;

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

    public Operator getOperationId() {
        return this.operationId;
    }

    public Record operationId(Operator operationId) {
        this.setOperationId(operationId);
        return this;
    }

    public void setOperationId(Operator operationId) {
        this.operationId = operationId;
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

    public String getOperationRespose() {
        return this.operationRespose;
    }

    public Record operationRespose(String operationRespose) {
        this.setOperationRespose(operationRespose);
        return this;
    }

    public void setOperationRespose(String operationRespose) {
        this.operationRespose = operationRespose;
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

    public User getUserId() {
        return this.userId;
    }

    public void setUserId(User user) {
        this.userId = user;
    }

    public Record userId(User user) {
        this.setUserId(user);
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
            ", operationId='" + getOperationId() + "'" +
            ", amount=" + getAmount() +
            ", userBalance=" + getUserBalance() +
            ", operationRespose='" + getOperationRespose() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
