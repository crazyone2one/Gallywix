package cn.master.gallywix.common.exception;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/**
 * @author 11's papa
 * @since 11/22/2023
 **/
@Data
@Builder
public class ExceptionData {
    @Singular
    private final List<Object> errors;
}
