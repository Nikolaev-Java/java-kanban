package exception;

public class ManagerCreateException extends RuntimeException{
    public ManagerCreateException() {
        super();
    }

    public ManagerCreateException(String message) {
        super(message);
    }
}
