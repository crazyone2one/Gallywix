package cn.master.gallywix.dto;

import cn.master.gallywix.entity.CustomFieldTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Created by 11's papa on 11/29/2023
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomFieldTemplateDao extends CustomFieldTemplate {
    private String name;

    private String scene;

    private String type;

    private String remark;

    private String options;

    private Boolean system;
}
