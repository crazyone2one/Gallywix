package cn.master.gallywix.controller.vo.workspace.AddMemberRequest;

import lombok.Data;

import java.util.List;

/**
 * @author 11's papa
 * @since 11/23/2023
 **/
@Data
public class AddMemberRequest {
    private String workspaceId;
    private List<String> userIds;
    private List<String> roleIds;
    private List<String> groupIds;
    private String projectId;
}
