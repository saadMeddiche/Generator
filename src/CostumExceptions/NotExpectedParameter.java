package CostumExceptions;

public class NotExpectedParameter extends RuntimeException {

    public NotExpectedParameter(String errorMessage) {
        super(errorMessage);
    }
}
