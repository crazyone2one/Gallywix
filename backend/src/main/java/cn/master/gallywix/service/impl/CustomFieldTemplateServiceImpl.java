package cn.master.gallywix.service.impl;

import cn.master.gallywix.dto.CustomFieldDao;
import cn.master.gallywix.dto.CustomFieldTemplateDao;
import cn.master.gallywix.entity.CustomFieldTemplate;
import cn.master.gallywix.mapper.CustomFieldTemplateMapper;
import cn.master.gallywix.service.ICustomFieldTemplateService;
import cn.master.gallywix.utils.ServiceUtils;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cn.master.gallywix.entity.table.CustomFieldTableDef.CUSTOM_FIELD;
import static cn.master.gallywix.entity.table.CustomFieldTemplateTableDef.CUSTOM_FIELD_TEMPLATE;

/**
 *  服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
public class CustomFieldTemplateServiceImpl extends ServiceImpl<CustomFieldTemplateMapper, CustomFieldTemplate> implements ICustomFieldTemplateService {

    @Override
    public List<CustomFieldTemplate> getSystemFieldCreateTemplate(CustomFieldDao customField, String templateId) {
        List<CustomFieldTemplate> fieldTemplates = mapper.selectListByQuery(
                QueryChain.of(CustomFieldTemplate.class).where(CUSTOM_FIELD_TEMPLATE.TEMPLATE_ID.eq(templateId))
        );
        for (CustomFieldTemplate fieldTemplate : fieldTemplates) {
            // 将全局字段替换成项目下的字段
            if (StringUtils.equals(fieldTemplate.getFieldId(), customField.getOriginGlobalId())) {
                fieldTemplate.setFieldId(customField.getId());
                break;
            }
        }
        return fieldTemplates;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(List<CustomFieldTemplate> customFields, String templateId, String scene) {
        if (CollectionUtils.isNotEmpty(customFields)) {
            long nextOrder = ServiceUtils.getNextOrder(templateId, mapper::getLastOrder);
            for (CustomFieldTemplate item : customFields) {
                item.setTemplateId(templateId);
                item.setScene(scene);
                if (item.getRequired() == null) {
                    item.setRequired(false);
                }
                nextOrder += ServiceUtils.ORDER_STEP;
                item.setOrder(nextOrder);
                mapper.insert(item);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateProjectTemplateGlobalField(CustomFieldDao customField, List<String> templateIds) {
        if (CollectionUtils.isEmpty(templateIds)) {
            return 0;
        }
        CustomFieldTemplate customFieldTemplate = new CustomFieldTemplate();
        customFieldTemplate.setFieldId(customField.getId());
        return mapper.updateByQuery(customFieldTemplate, QueryWrapper.create()
                .where(CUSTOM_FIELD_TEMPLATE.FIELD_ID.eq(customField.getOriginGlobalId()))
                .and(CUSTOM_FIELD_TEMPLATE.TEMPLATE_ID.in(templateIds)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTemplateId(String templateId) {
        mapper.deleteByQuery(QueryChain.of(CustomFieldTemplate.class).where(CUSTOM_FIELD_TEMPLATE.TEMPLATE_ID.eq(templateId)));
    }

    @Override
    public List<CustomFieldTemplateDao> list(CustomFieldTemplate request) {
        QueryChain<CustomFieldTemplate> queryChain = QueryChain.of(CustomFieldTemplate.class)
                .select(CUSTOM_FIELD_TEMPLATE.FIELD_ID, CUSTOM_FIELD_TEMPLATE.TEMPLATE_ID, CUSTOM_FIELD_TEMPLATE.REQUIRED,
                        CUSTOM_FIELD_TEMPLATE.DEFAULT_VALUE, CUSTOM_FIELD_TEMPLATE.CUSTOM_DATA,
                        CUSTOM_FIELD_TEMPLATE.ID.as("id"),
                        CUSTOM_FIELD.NAME.as("name"),CUSTOM_FIELD.TYPE.as("type"),CUSTOM_FIELD.REMARK.as("remark"),
                        CUSTOM_FIELD.SYSTEM.as("system"),CUSTOM_FIELD.OPTIONS.as("options"))
                .from(CUSTOM_FIELD_TEMPLATE.as("cft"))
                .innerJoin(CUSTOM_FIELD.as("cf").getClass()).on(CUSTOM_FIELD_TEMPLATE.FIELD_ID.eq(CUSTOM_FIELD.ID))
                .where(CUSTOM_FIELD_TEMPLATE.TEMPLATE_ID.eq(request.getTemplateId()))
                .orderBy(CUSTOM_FIELD_TEMPLATE.ORDER.asc());
        return mapper.selectListByQueryAs(queryChain, CustomFieldTemplateDao.class);
    }
}
