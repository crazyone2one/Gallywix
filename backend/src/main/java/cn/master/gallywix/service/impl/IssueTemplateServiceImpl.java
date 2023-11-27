package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.constants.TemplateConstants;
import cn.master.gallywix.dto.CustomFieldDao;
import cn.master.gallywix.entity.CustomFieldTemplate;
import cn.master.gallywix.entity.IssueTemplate;
import cn.master.gallywix.mapper.IssueTemplateMapper;
import cn.master.gallywix.service.ICustomFieldTemplateService;
import cn.master.gallywix.service.IIssueTemplateService;
import cn.master.gallywix.service.ISystemProjectService;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.master.gallywix.entity.table.IssueTemplateTableDef.ISSUE_TEMPLATE;

/**
 * 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class IssueTemplateServiceImpl extends ServiceImpl<IssueTemplateMapper, IssueTemplate> implements IIssueTemplateService {
    final ISystemProjectService projectService;
    final ICustomFieldTemplateService customFieldTemplateService;

    @Override
    public void handleSystemFieldCreate(CustomFieldDao customFieldDao) {
        List<IssueTemplate> issueTemplates = QueryChain.of(IssueTemplate.class).where(ISSUE_TEMPLATE.GLOBAL.eq(true)
                .or(ISSUE_TEMPLATE.PROJECT_ID.eq(customFieldDao.getProjectId()))).list();
        Map<Boolean, List<IssueTemplate>> templatesMap = issueTemplates.stream()
                .collect(Collectors.groupingBy(IssueTemplate::getGlobal));
        // 获取全局模板
        List<IssueTemplate> globalTemplates = templatesMap.get(true);
        // 获取当前工作空间下模板
        List<IssueTemplate> projectTemplates = templatesMap.get(false);
        globalTemplates.forEach(global -> {
            List<IssueTemplate> projectTemplate = null;
            if (CollectionUtils.isNotEmpty(projectTemplates)) {
                projectTemplate = projectTemplates.stream()
                        .filter(i -> i.getName().equals(global.getName()))
                        .collect(Collectors.toList());
            }
            // 如果没有项目级别的模板就创建
            if (CollectionUtils.isEmpty(projectTemplate)) {
                IssueTemplate template = new IssueTemplate();
                BeanUtils.copyProperties(global, template);
                template.setGlobal(false);
                template.setProjectId(customFieldDao.getProjectId());
                mapper.insert(template);
                projectService.updateIssueTemplate(global.getId(), template.getId(), customFieldDao.getProjectId());
                List<CustomFieldTemplate> customFieldTemplate =
                        customFieldTemplateService.getSystemFieldCreateTemplate(customFieldDao, global.getId());
                customFieldTemplateService.create(customFieldTemplate, template.getId(),
                        TemplateConstants.FieldTemplateScene.ISSUE.name());
            }
        });
        if (CollectionUtils.isNotEmpty(projectTemplates)) {
            customFieldTemplateService.updateProjectTemplateGlobalField(customFieldDao,
                    projectTemplates.stream().map(IssueTemplate::getId).collect(Collectors.toList()));
        }
    }
}
