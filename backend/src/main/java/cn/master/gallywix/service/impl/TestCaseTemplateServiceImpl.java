package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.constants.TemplateConstants;
import cn.master.gallywix.common.exception.CustomException;
import cn.master.gallywix.controller.vo.BaseQueryVO;
import cn.master.gallywix.controller.vo.template.UpdateCaseFieldTemplateRequest;
import cn.master.gallywix.dto.CustomFieldDao;
import cn.master.gallywix.entity.CustomFieldTemplate;
import cn.master.gallywix.entity.TestCaseTemplate;
import cn.master.gallywix.mapper.TestCaseTemplateMapper;
import cn.master.gallywix.service.ICustomFieldTemplateService;
import cn.master.gallywix.service.ISystemProjectService;
import cn.master.gallywix.service.ITestCaseTemplateService;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.master.gallywix.entity.table.TestCaseTemplateTableDef.TEST_CASE_TEMPLATE;
import static com.mybatisflex.core.query.QueryMethods.notExists;
import static com.mybatisflex.core.query.QueryMethods.select;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(UpdateCaseFieldTemplateRequest request) {
        checkExist(request);
        TestCaseTemplate template = new TestCaseTemplate();
        BeanUtils.copyProperties(request, template);
        template.setGlobal(false);
        template.setUpdateUser(SessionUtils.getUserId());
        template.setCreateUser(SessionUtils.getUserId());
        if (Objects.isNull(template.getSystem())) {
            template.setSystem(false);
        }
        mapper.insert(template);
        customFieldTemplateService.create(request.getCustomFields(), template.getId(),
                TemplateConstants.FieldTemplateScene.TEST_CASE.name());
        return template.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UpdateCaseFieldTemplateRequest request) {
        if (request.getGlobal() != null && request.getGlobal()) {
            String originId = request.getId();
            // 如果是全局字段，则创建对应工作空间字段
            String id = add(request);
            projectService.updateCaseTemplate(originId, id, request.getProjectId());
        } else {
            checkExist(request);
            customFieldTemplateService.deleteByTemplateId(request.getId());
            TestCaseTemplate template = new TestCaseTemplate();
            BeanUtils.copyProperties(request, template);
            template.setUpdateUser(SessionUtils.getUserId());
            mapper.update(template);
            customFieldTemplateService.create(request.getCustomFields(), template.getId(),
                    TemplateConstants.FieldTemplateScene.TEST_CASE.name());
        }
    }

    @Override
    public Page<TestCaseTemplate> queryPage(BaseQueryVO<TestCaseTemplate> page) {
        QueryWrapper wrapper = QueryChain.create().from(TEST_CASE_TEMPLATE.as("tcft"))
                .where(TEST_CASE_TEMPLATE.NAME.like(page.getName()))
                .and(TEST_CASE_TEMPLATE.PROJECT_ID.eq(page.getProjectId())
                        .or(
                                TEST_CASE_TEMPLATE.GLOBAL.eq(true)
                                        .and(notExists(select(TEST_CASE_TEMPLATE.ID).from(TEST_CASE_TEMPLATE.as("tcft_child"))
                                                .where(" tcft_child.name = tcft.name")
                                                .and("tcft_child.global != 1")
                                                .and("tcft_child.project_id='" + page.getProjectId() + "'")))).when(Objects.nonNull(page.getProjectId())))
                .and(TEST_CASE_TEMPLATE.WORKSPACE_ID.eq(page.getWorkspaceId())
                        .or(
                                TEST_CASE_TEMPLATE.GLOBAL.eq(true)
                                        .and(notExists(select(TEST_CASE_TEMPLATE.ID).from(TEST_CASE_TEMPLATE.as("tcft_child"))
                                                .where(" tcft_child.name = tcft.name")
                                                .and("tcft_child.global != 1")
                                                .and("tcft_child.workspace_id='" + page.getWorkspaceId() + "'")))).when(Objects.nonNull(page.getWorkspaceId())));
        return mapper.paginate(page.getPageNumber(), page.getPageSize(), wrapper);
    }

    private void checkExist(TestCaseTemplate testCaseTemplate) {
        if (Objects.nonNull(testCaseTemplate.getName())) {
            boolean exists = QueryChain.of(TestCaseTemplate.class)
                    .where(TEST_CASE_TEMPLATE.NAME.eq(testCaseTemplate.getName())
                            .and(TEST_CASE_TEMPLATE.PROJECT_ID.eq(testCaseTemplate.getProjectId())))
                    .where(TEST_CASE_TEMPLATE.ID.ne(testCaseTemplate.getId()))
                    .exists();
            if (exists) {
                CustomException.throwException("工作空间下已存在该模板:" + testCaseTemplate.getName());
            }
        }
    }
}
