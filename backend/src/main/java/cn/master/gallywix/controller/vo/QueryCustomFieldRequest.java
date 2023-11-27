package cn.master.gallywix.controller.vo;

import cn.master.gallywix.entity.CustomField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author 11's papa
 * @since 11/27/2023
 **/
@Setter
@Getter
public class QueryCustomFieldRequest extends BaseQueryVO<CustomField> {
    private String templateId;
    private String workspaceId;
    private String scene;
    private List<String> templateContainIds;
}
