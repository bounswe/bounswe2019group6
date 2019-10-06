package cmpe451.group6.authorization.dto;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

public class StringResponseWrapper {

    private final String message;

    public StringResponseWrapper(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
