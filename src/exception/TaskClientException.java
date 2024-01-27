package exception;

public class TaskClientException extends RuntimeException{
    public TaskClientException() {
        super();
    }

    public TaskClientException(String message) {
        super(message);
    }

    public TaskClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
