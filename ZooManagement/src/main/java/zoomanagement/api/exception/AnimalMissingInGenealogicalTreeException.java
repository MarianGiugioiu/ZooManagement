package zoomanagement.api.exception;

public class AnimalMissingInGenealogicalTreeException extends Exception{
    public AnimalMissingInGenealogicalTreeException() {
    }

    public AnimalMissingInGenealogicalTreeException(String message) {
        super(message);
    }
}
