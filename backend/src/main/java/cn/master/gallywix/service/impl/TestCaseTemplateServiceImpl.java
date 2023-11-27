package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.constants.TemplateConstants;
import cn.master.gallywix.dto.CustomFieldDao;
import cn.master.gallywix.entity.CustomFieldTemplate;
import cn.master.gallywix.entity.TestCaseTemplate;
import cn.master.gallywix.mapper.TestCaseTemplateMapper;
import cn.master.gallywix.service.ICustomFieldTemplateService;
import cn.master.gallywix.service.ISystemProjectService;
import cn.master.gallywix.service.ITestCaseTemplateService;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.master.gallywix.entity.table.TestCaseTemplateTableDef.TEST_CASE_TEMPLATE;

/**
 * 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class TestCaseTemplateServiceImpl extends ServiceImpl<TestCaseTemplateMapper, TestCaseTemplate> implements ITestCaseTemplateService {
    final ISystemProjectService projectService;
    final ICustomFieldTemplateService customFieldTemplateService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleSystemFieldCreate(CustomFieldDao customField) {
        List<TestCaseTemplate> testCaseTemplates = mapper.selectListByQuery(QueryChain.create()
                .where(TEST_CASE_TEMPLATE.GLOBAL.eq(true))
                .or(TEST_CASE_TEMPLATE.PROJECT_ID.eq(customField.getProjectId())));
        Map<Boolean, List<TestCaseTemplate>> templatesMap = testCaseTemplates.stream().collect(Collectors.groupingBy(TestCaseTemplate::getGlobal));
        // 获取全局模板
        List<TestCaseTemplate> globalTemplates = templatesMap.get(true);
        // 获取当前工作空间下模板
        List<TestCaseTemplate> projectTemplates = templatesMap.get(false);
        globalTemplates.forEach(global -> {
            List<TestCaseTemplate> projectTemplate = null;
            if (CollectionUtils.isNotEmpty(projectTemplates)) {
                projectTemplate = projectTemplates.stream()
                        .filter(i -> i.getName().equals(global.getName()))
                        .collect(Collectors.toList());
            }
            // 如果没有项目级别的模板就创建
            if (CollectionUtils.isEmpty(projectTemplate)) {
                TestCaseTemplate template = new TestCaseTemplate();
                BeanUtils.copyProperties(global, template);
                template.setGlobal(false);
                template.setProjectId(customField.getProjectId());
                mapper.insert(template);
                projectService.updateCaseTemplate(global.getId(), template.getId(), customField.getProjectId());
                List<CustomFieldTemplate> customFieldTemplate = customFieldTemplateService.getSystemFieldCreateTemplate(customField, global.getId());
                customFieldTemplateService.create(customFieldTemplate, template.getId(),
                        TemplateConstants.FieldTemplateScene.TEST_CASE.name());
            }
        });
        if (CollectionUtils.isNotEmpty(projectTemplates)) {
            customFieldTemplateService.updateProjectTemplateGlobalField(customField,
                    projectTemplates.stream().map(TestCaseTemplate::getId).collect(Collectors.toList()));
        }
    }
}
