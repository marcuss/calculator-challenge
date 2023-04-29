package pro.marcuss.calculator.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;
import pro.marcuss.calculator.domain.enumeration.Operator;

/**
 * A DTO for the {@link pro.marcuss.calculator.domain.Record} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecordDTO implements Serializable {

    private String id;

    private Operator operationId;

    @NotNull
    @DecimalMin(value = "0")
    private Double amount;

    @NotNull
    private Double userBalance;

    @NotNull
    private String operationRespose;

    private Instant date;

    private UserDTO user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Operator getOperationId() {
        return operationId;
    }

    public void setOperationId(Operator operationId) {
        this.operationId = operationId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(Double userBalance) {
        this.userBalance = userBalance;
    }

    public String getOperationRespose() {
        return operationRespose;
    }

    public void setOperationRespose(String operationRespose) {
        this.operationRespose = operationRespose;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
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
        if (!(o instanceof RecordDTO)) {
            return false;
        }

        RecordDTO recordDTO = (RecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecordDTO{" +
            "id='" + getId() + "'" +
            ", operationId='" + getOperationId() + "'" +
            ", amount=" + getAmount() +
            ", userBalance=" + getUserBalance() +
            ", operationRespose='" + getOperationRespose() + "'" +
            ", date='" + getDate() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
