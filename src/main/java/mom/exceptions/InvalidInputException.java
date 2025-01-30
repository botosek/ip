package mom.exceptions;

public class InvalidInputException extends Exception {
    public InvalidInputException(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public String toString() {
        return "Invalid input detected:" + "\n" + getMessage();
    }
}
