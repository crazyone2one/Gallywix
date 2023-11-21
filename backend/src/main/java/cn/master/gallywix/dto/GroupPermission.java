package cn.master.gallywix.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 11's papa
 * @since 11/16/2023
 **/
@Data
public class GroupPermission implements Serializable {
    private String id;
    private String name;
    private String resourceId;
    private Boolean checked = false;
    private Boolean license = false;
    @Serial
    private static final long serialVersionUID = 1L;
}
