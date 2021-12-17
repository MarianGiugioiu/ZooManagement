package zoomanagement.api.exception;

public class PenAlreadyUsedException extends Exception{
    public PenAlreadyUsedException() {
    }

    public PenAlreadyUsedException(String message) {
        super(message);
    }
}
