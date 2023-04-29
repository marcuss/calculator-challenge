package pro.marcuss.calculator.web.rest.errors;


import static pro.marcuss.calculator.web.rest.errors.ErrorConstants.OPERATION_COST_ALREADY_DEFINED;

public class OperationCostAlreadyDefinedException extends BadRequestAlertException{

    public OperationCostAlreadyDefinedException() {
        super(OPERATION_COST_ALREADY_DEFINED, "Operation cost already defined, try modifying it.", "operation", "operation-cost-already-defined");
    }
}
