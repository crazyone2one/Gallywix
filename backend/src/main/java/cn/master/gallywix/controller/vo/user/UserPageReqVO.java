package cn.master.gallywix.controller.vo.user;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 11's papa
 * @since 11/06/2023
 **/
@Setter
@Getter
public class UserPageReqVO  {
    private String name;
    private String workspaceId;
    private String projectId;
    private String userGroupId;
    private long pageNumber;
    private long pageSize;
}
