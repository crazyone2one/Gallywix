package cn.master.gallywix.controller.vo.template;

import cn.master.gallywix.entity.CustomFieldTemplate;
import cn.master.gallywix.entity.TestCaseTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Created by 11's papa on 11/29/2023
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateCaseFieldTemplateRequest extends TestCaseTemplate {
    List<CustomFieldTemplate> customFields;
}
