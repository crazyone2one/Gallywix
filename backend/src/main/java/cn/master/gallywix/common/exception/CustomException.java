package cn.master.gallywix.common.exception;

/**
 * @author 11's papa
 * @since 11/02/2023
 **/
public class CustomException extends RuntimeException {
    private CustomException(String message) {
        super(message);
    }

    private CustomException(Throwable t) {
        super(t);
    }

    public static void throwException(String message) {
        throw new CustomException(message);
    }

    public static CustomException getException(String message) {
        throw new CustomException(message);
    }

    public static void throwException(Throwable t) {
        throw new CustomException(t);
    }
}
