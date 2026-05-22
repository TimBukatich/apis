package by.bsu.computerfirm.exception;

public class ComponentValidationException extends ComputerFirmException {

    private static final long serialVersionUID = 1L;

    public ComponentValidationException() {
        super();
    }

    public ComponentValidationException(String message) {
        super(message);
    }

    public ComponentValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentValidationException(Throwable cause) {
        super(cause);
    }
}
