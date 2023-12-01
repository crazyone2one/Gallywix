package cn.master.gallywix.common.exception;

import cn.master.gallywix.common.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author 11's papa
 * @since 11/07/2023
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseBody
    @ExceptionHandler({AuthenticationException.class})
    public ResponseResult<Exception> handleAuthenticationException(Exception ex) {
        log.error("handleAccessDeniedException ", ex);
        return ResponseResult.fail(HttpStatus.UNAUTHORIZED.value(),ex.getLocalizedMessage());
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
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseResult<Exception> handleGlobalException(Exception exception) {
        log.error("handleGlobalException : {}", exception.getMessage());
        log.error("handleGlobalException : ", exception);
        return ResponseResult.fail(StringUtils.isNoneBlank(exception.getLocalizedMessage()) ? exception.getLocalizedMessage() : "系统内部错误", exception);
    }
}
