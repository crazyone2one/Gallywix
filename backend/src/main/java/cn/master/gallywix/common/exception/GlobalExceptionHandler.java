package cn.master.gallywix.common.exception;

import cn.master.gallywix.common.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 11's papa
 * @since 11/07/2023
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("code", HttpStatus.UNAUTHORIZED.toString());
        error.put("message", "Authentication failed at controller advice");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseResult<AccessDeniedException> handleAccessDeniedException(Exception ex) {
        log.error("handleAccessDeniedException : {}", ex.getMessage());
        return ResponseResult.fail(HttpStatus.FORBIDDEN.value(), "权限异常：无访问权限");
    }

    @ExceptionHandler({CustomException.class})
    public ResponseResult<CustomException> handleCustomException(CustomException exception) {
        log.error("handleCustomException : {}", exception.getLocalizedMessage());
        return ResponseResult.fail(exception.getLocalizedMessage(), exception);
    }

    // global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseResult<Exception> handleGlobalException(Exception exception) {
        log.error("handleGlobalException : {}", exception.getMessage());
        return ResponseResult.fail(exception.getLocalizedMessage(), exception);
    }
}
