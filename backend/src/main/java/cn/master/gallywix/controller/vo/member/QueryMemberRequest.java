package cn.master.gallywix.controller.vo.member;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 11's papa
 * @since 11/18/2023
 **/
@Setter
@Getter
public class QueryMemberRequest {
    private String name;
    private String workspaceId;
    private String projectId;
}
