package exception;


public class ManagerSaveException extends Error {
    public ManagerSaveException() {
    }

    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerSaveException(String message) {
        super(message);
    }
}
