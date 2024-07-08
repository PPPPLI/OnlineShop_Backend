package back.api.exceptions;

public class UserDoesntExistException extends RuntimeException{

    public UserDoesntExistException(String message) {
        super(message);
    }

    public UserDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesntExistException() {
        super("User doesn't exist");
    }
}
