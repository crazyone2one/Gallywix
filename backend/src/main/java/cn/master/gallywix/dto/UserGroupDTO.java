package cn.master.gallywix.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 11's papa
 * @since 11/16/2023
 **/
@Setter
@Getter
public class UserGroupDTO {
    private String userId;
    private String groupId;
    private String sourceId;
    private String name;
    /**
     * 用户组所属类型
     */
    private String type;
}
