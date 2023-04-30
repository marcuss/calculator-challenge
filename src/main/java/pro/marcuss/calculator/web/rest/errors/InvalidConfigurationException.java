package pro.marcuss.calculator.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class InvalidConfigurationException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private String entityName;

    private String errorKey;

    public InvalidConfigurationException(String defaultMessage, String entityName, String errorKey) {
        super(
            ErrorConstants.INVALID_CONFIGURATION_TYPE,
            "Invalid Configuration: " + defaultMessage,
            Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName, errorKey)
        );
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        return parameters;
    }
}
