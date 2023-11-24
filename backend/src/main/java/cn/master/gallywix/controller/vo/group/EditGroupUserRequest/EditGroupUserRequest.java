package cn.master.gallywix.controller.vo.group.EditGroupUserRequest;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author 11's papa
 * @since 11/24/2023
 **/
@Setter
@Getter
public class EditGroupUserRequest {
    private List<String> userIds;
    private List<String> sourceIds;
    private String groupId;
}
