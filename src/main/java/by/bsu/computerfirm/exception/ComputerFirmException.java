package by.bsu.computerfirm.exception;

public class ComputerFirmException extends Exception {

    private static final long serialVersionUID = 1L;

    public ComputerFirmException() {
        super();
    }

    public ComputerFirmException(String message) {
        super(message);
    }

    public ComputerFirmException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComputerFirmException(Throwable cause) {
        super(cause);
    }
}
