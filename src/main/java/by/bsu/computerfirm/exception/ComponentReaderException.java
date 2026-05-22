package by.bsu.computerfirm.exception;

public class ComponentReaderException extends ComputerFirmException {

    private static final long serialVersionUID = 1L;

    public ComponentReaderException() {
        super();
    }

    public ComponentReaderException(String message) {
        super(message);
    }

    public ComponentReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentReaderException(Throwable cause) {
        super(cause);
    }
}
