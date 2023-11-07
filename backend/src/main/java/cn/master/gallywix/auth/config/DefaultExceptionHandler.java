package cn.master.gallywix.auth.config;

import cn.master.gallywix.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 11's papa
 * @since 11/07/2023
 **/
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        Map<String,String > error = new HashMap<>();
        error.put("code", HttpStatus.UNAUTHORIZED.toString());
        error.put("message", "Authentication failed at controller advice");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<Object> handleCustomException(CustomException exception) {
        Map<String,String > error = new HashMap<>();
        error.put("code", HttpStatus.BAD_REQUEST.toString());
        error.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
