package cn.master.gallywix.common.exception;

import cn.master.gallywix.common.result.ResponseResult;
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
    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("code", HttpStatus.UNAUTHORIZED.toString());
        error.put("message", "Authentication failed at controller advice");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<Object> handleCustomException(CustomException exception) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseResult.fail(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }
}
