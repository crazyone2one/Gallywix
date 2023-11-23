package cn.master.gallywix.controller.vo.user;

import lombok.Data;

import java.util.List;

/**
 * @author 11's papa
 * @since 11/13/2023
 **/
@Data
public class AddOrgMemberRequestVO {
    private String organizationId;
    private List<String> userIds;
}
