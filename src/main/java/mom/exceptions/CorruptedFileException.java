package mom.exceptions;

public class CorruptedFileException extends Exception {
    public CorruptedFileException(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public String toString() {
        return "Corrupted file detected:" + "\n" + getMessage() + "\n";
    }
}
