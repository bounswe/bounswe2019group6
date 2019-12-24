package cmpe451.group6.authorization.dto;

public class StringResponseWrapper {

    private final String message;

    public StringResponseWrapper(Object message) {
        this.message = message.toString();
    }

    public String getMessage() {
        return message;
    }
}
