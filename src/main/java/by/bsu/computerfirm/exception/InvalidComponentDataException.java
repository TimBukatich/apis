package by.bsu.computerfirm.exception;

public class InvalidComponentDataException extends ComputerFirmException {

    private static final long serialVersionUID = 1L;

    public InvalidComponentDataException() {
        super();
    }

    public InvalidComponentDataException(String message) {
        super(message);
    }

    public InvalidComponentDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidComponentDataException(Throwable cause) {
        super(cause);
    }
}
