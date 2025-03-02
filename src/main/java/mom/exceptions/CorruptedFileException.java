package mom.exceptions;

/**
 * Exception for files with incorrect format
 */
public class CorruptedFileException extends Exception {
    public CorruptedFileException(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public String toString() {
        return "Corrupted file detected:" + "\n" + getMessage() + "\n";
    }
}
