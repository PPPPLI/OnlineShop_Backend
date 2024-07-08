package back.api.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler({UserDoesntExistException.class, UserAlreadyExistsException.class, BadRequestException.class, AuthenticationException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleException(RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
}
