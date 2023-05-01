package pro.marcuss.calculator.service.dto;

import pro.marcuss.calculator.domain.enumeration.Operator;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link pro.marcuss.calculator.domain.Record} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecordDTO implements Serializable {

    private String id;

    private String userLogin;

    private Boolean active;

    @NotNull
    private Operator operation;

    @NotNull
    private Double amount;

    private Double userBalance;

    private String operationResponse;

    private Instant date;

    private UserDTO user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserLogin() {
        if (user != null) {
            return user.getLogin();
        }
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Operator getOperation() {
        return operation;
    }

    public void setOperation(Operator operation) {
        this.operation = operation;
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

    public String getOperationResponse() {
        return operationResponse;
    }

    public void setOperationResponse(String operationResponse) {
        this.operationResponse = operationResponse;
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
