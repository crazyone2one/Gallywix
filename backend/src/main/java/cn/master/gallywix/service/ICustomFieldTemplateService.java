package cn.master.gallywix.service;

import cn.master.gallywix.dto.CustomFieldDao;
import cn.master.gallywix.dto.CustomFieldTemplateDao;
import cn.master.gallywix.entity.CustomFieldTemplate;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 *  服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface ICustomFieldTemplateService extends IService<CustomFieldTemplate> {

    List<CustomFieldTemplate> getSystemFieldCreateTemplate(CustomFieldDao customField, String templateId);

    void create(List<CustomFieldTemplate> customFieldTemplate, String id, String name);

    int updateProjectTemplateGlobalField(CustomFieldDao customField, List<String> collect);

    void deleteByTemplateId(String templateId);

    List<CustomFieldTemplateDao> list(CustomFieldTemplate request);
}
