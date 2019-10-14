package cmpe451.group6.authorization.exception;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.annotations.Entity;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

  public static final String GENERIC_ERROR_RESPONSE = "This is a generic error response. " +
          "That means this type of exception is not handled explicitly." +
          "Please see the exception on " +
          "application log and report this to the backend team " +
          "(e.g. open an issue) with the producer request. ";

  @Bean
  public ErrorAttributes errorAttributes() {
    // Hide exception field in the return object
    return new DefaultErrorAttributes() {
      @Override
      public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
        errorAttributes.remove("exception");
        return errorAttributes;
      }
    };
  }

  @ExceptionHandler(CustomException.class)
  public void handleCustomException(HttpServletResponse res, CustomException ex) throws IOException {
    res.sendError(ex.getHttpStatus().value(), ex.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public void handleAccessDeniedException(HttpServletResponse res) throws IOException {
    res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
  }

  // Generic handler for all exceptions.
  @ExceptionHandler(Exception.class)
  public void handleException(HttpServletResponse res) throws IOException {
    res.sendError(HttpStatus.BAD_REQUEST.value(), GENERIC_ERROR_RESPONSE);
  }

  @ExceptionHandler(MissingServletRequestPartException.class)
  public void handleMissingParts(HttpServletResponse res) throws IOException {
    res.sendError(HttpStatus.BAD_REQUEST.value(),"Missing parts on request part");
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public void handleMissingParams(HttpServletResponse res) throws IOException {
    res.sendError(HttpStatus.BAD_REQUEST.value(),"Missing parameter on request");
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public void handleBadJSON(HttpServletResponse res) throws IOException {
    res.sendError(HttpStatus.EXPECTATION_FAILED.value(),"Invalid JSON data");
  }
}
