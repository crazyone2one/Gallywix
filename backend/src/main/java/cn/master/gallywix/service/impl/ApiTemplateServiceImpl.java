package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.constants.TemplateConstants;
import cn.master.gallywix.dto.CustomFieldDao;
import cn.master.gallywix.entity.ApiTemplate;
import cn.master.gallywix.entity.CustomFieldTemplate;
import cn.master.gallywix.mapper.ApiTemplateMapper;
import cn.master.gallywix.service.IApiTemplateService;
import cn.master.gallywix.service.ICustomFieldTemplateService;
import cn.master.gallywix.service.ISystemProjectService;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static cn.master.gallywix.entity.table.ApiTemplateTableDef.API_TEMPLATE;

/**
 * api模版表 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ApiTemplateServiceImpl extends ServiceImpl<ApiTemplateMapper, ApiTemplate> implements IApiTemplateService {
    final ICustomFieldTemplateService customFieldTemplateService;
    final ISystemProjectService projectService;

    @Override
    public void handleSystemFieldCreate(CustomFieldDao customField) {
        List<ApiTemplate> apiTemplates = QueryChain.of(ApiTemplate.class).where(API_TEMPLATE.GLOBAL.eq(true)
                .or(API_TEMPLATE.PROJECT_ID.eq(customField.getProjectId()))).list();
        Map<Boolean, List<ApiTemplate>> templatesMap = apiTemplates.stream()
                .collect(Collectors.groupingBy(ApiTemplate::getGlobal));
        // 获取全局模板
        List<ApiTemplate> globalTemplates = templatesMap.get(true);
        // 获取当前工作空间下模板
        List<ApiTemplate> projectTemplates = templatesMap.get(false);
        globalTemplates.forEach(global -> {
            List<ApiTemplate> projectTemplate = null;
            if (CollectionUtils.isNotEmpty(projectTemplates)) {
                projectTemplate = projectTemplates.stream()
                        .filter(i -> i.getName().equals(global.getName()))
                        .collect(Collectors.toList());
            }
            // 如果没有项目级别的模板就创建
            if (CollectionUtils.isEmpty(projectTemplate)) {
                ApiTemplate template = new ApiTemplate();
                BeanUtils.copyProperties(global, template);
                template.setId(UUID.randomUUID().toString());
                template.setGlobal(false);
                template.setProjectId(customField.getProjectId());
                mapper.insert(template);
                projectService.updateApiTemplate(global.getId(), template.getId(), customField.getProjectId());
                List<CustomFieldTemplate> customFieldTemplate =
                        customFieldTemplateService.getSystemFieldCreateTemplate(customField, global.getId());

                customFieldTemplateService.create(customFieldTemplate, template.getId(),
                        TemplateConstants.FieldTemplateScene.API.name());
            }
        });
        if (CollectionUtils.isNotEmpty(projectTemplates)) {
            customFieldTemplateService.updateProjectTemplateGlobalField(customField,
                    projectTemplates.stream().map(ApiTemplate::getId).collect(Collectors.toList()));
        }
    }
}
