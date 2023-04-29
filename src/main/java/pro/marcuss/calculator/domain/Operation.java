package pro.marcuss.calculator.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pro.marcuss.calculator.domain.enumeration.Operator;

/**
 * A Operation.
 */
@Document(collection = "operation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Operation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("operator")
    private Operator operator;

    @NotNull
    @DecimalMin(value = "0")
    @Field("cost")
    private Double cost;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Operation id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Operator getOperator() {
        return this.operator;
    }

    public Operation operator(Operator operator) {
        this.setOperator(operator);
        return this;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Double getCost() {
        return this.cost;
    }

    public Operation cost(Double cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operation)) {
            return false;
        }
        return id != null && id.equals(((Operation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Operation{" +
            "id=" + getId() +
            ", operator='" + getOperator() + "'" +
            ", cost=" + getCost() +
            "}";
    }
}