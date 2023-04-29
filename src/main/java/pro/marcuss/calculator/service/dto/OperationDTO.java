package pro.marcuss.calculator.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import pro.marcuss.calculator.domain.enumeration.Operator;

/**
 * A DTO for the {@link pro.marcuss.calculator.domain.Operation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OperationDTO implements Serializable {

    private String id;

    @NotNull
    private Operator operator;

    @NotNull
    @DecimalMin(value = "0")
    private Double cost;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperationDTO)) {
            return false;
        }

        OperationDTO operationDTO = (OperationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, operationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OperationDTO{" +
            "id='" + getId() + "'" +
            ", operator='" + getOperator() + "'" +
            ", cost=" + getCost() +
            "}";
    }
}
