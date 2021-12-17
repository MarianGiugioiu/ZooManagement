package zoomanagement.api.exception;

public class EmployeeBusyException extends Exception{
    public EmployeeBusyException() {
    }

    public EmployeeBusyException(String message) {
        super(message);
    }
}
