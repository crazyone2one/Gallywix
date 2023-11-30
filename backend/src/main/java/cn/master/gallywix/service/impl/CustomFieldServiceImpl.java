package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.constants.TemplateConstants;
import cn.master.gallywix.common.exception.CustomException;
import cn.master.gallywix.controller.vo.QueryCustomFieldRequest;
import cn.master.gallywix.dto.CustomFieldDao;
import cn.master.gallywix.entity.CustomField;
import cn.master.gallywix.mapper.CustomFieldMapper;
import cn.master.gallywix.service.IApiTemplateService;
import cn.master.gallywix.service.ICustomFieldService;
import cn.master.gallywix.service.IIssueTemplateService;
import cn.master.gallywix.service.ITestCaseTemplateService;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static cn.master.gallywix.entity.table.CustomFieldTableDef.CUSTOM_FIELD;
import static com.mybatisflex.core.query.QueryMethods.notExists;
import static com.mybatisflex.core.query.QueryMethods.select;

/**
 * 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomFieldServiceImpl extends ServiceImpl<CustomFieldMapper, CustomField> implements ICustomFieldService {

    final ITestCaseTemplateService testCaseTemplateService;
    final IIssueTemplateService iIssueTemplateService;
    final IApiTemplateService apiTemplateService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(CustomField customField) {
        checkExist(customField);
        customField.setGlobal(false);
        customField.setThirdPart(false);
        customField.setCreateUser(SessionUtils.getUserId());
        mapper.insert(customField);
        return customField.getId();
    }

    @Override
    public Page<CustomField> queryPage(QueryCustomFieldRequest request) {
        QueryWrapper wrapper = buildWrapper(request);
        return mapper.paginate(request.getPageNumber(), request.getPageSize(), wrapper);
    }

    private static QueryWrapper buildWrapper(QueryCustomFieldRequest request) {
        QueryWrapper wrapper = QueryChain.create().from(CUSTOM_FIELD.as("cf"))
                .where(CUSTOM_FIELD.NAME.like(request.getName()))
                .and(CUSTOM_FIELD.WORKSPACE_ID.eq(request.getWorkspaceId()).or(
                        CUSTOM_FIELD.GLOBAL.eq(true)
                                .and(notExists(
                                        select(CUSTOM_FIELD.ID).from(CUSTOM_FIELD.as("cf_child"))
                                                .where("cf_child.name = cf.name")
                                                .and("cf_child.scene = cf.scene")
                                                .and("cf_child.global != 1")
                                                .and("cf_child.workspace_id='" + request.getWorkspaceId() + "'")
                                ))
                ).when(Objects.nonNull(request.getWorkspaceId())))
                .and(CUSTOM_FIELD.PROJECT_ID.eq(request.getProjectId())
                        .or(CUSTOM_FIELD.GLOBAL.eq(true)
                                .and(
                                        notExists(
                                                select(CUSTOM_FIELD.ID).from(CUSTOM_FIELD.as("cf_child"))
                                                        .where("cf_child.name = cf.name")
                                                        .and("cf_child.scene = cf.scene")
                                                        .and("cf_child.global != 1")
                                                        .and("cf_child.project_id='" + request.getProjectId() + "'")
                                        )
                                )).when(Objects.nonNull(request.getProjectId())))
                .and(CUSTOM_FIELD.ID.in(request.getIds()))
                .and(CUSTOM_FIELD.ID.notIn(request.getTemplateId()));
        if (Objects.nonNull(request.getFilters())) {
            Set<Map.Entry<String, List<String>>> entries = request.getFilters().entrySet();
            for (Map.Entry<String, List<String>> entry : entries) {
                if (Objects.nonNull(entry.getValue())) {
                    if ("scene".equals(entry.getKey())) {
                        wrapper.and(CUSTOM_FIELD.SCENE.in(entry.getValue()));
                    } else if ("type".equals(entry.getKey())) {
                        wrapper.and(CUSTOM_FIELD.TYPE.in(entry.getValue()));
                    }
                }
            }
        }
        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateCustomField(CustomField customField) {
        if (Objects.nonNull(customField.getGlobal()) && customField.getGlobal()) {
            CustomFieldDao customFieldDao = new CustomFieldDao();
            BeanUtils.copyProperties(customField, customFieldDao);
            customFieldDao.setOriginGlobalId(customField.getId());
            // 如果是全局字段，则创建对应项目字段
            add(customFieldDao);
            if (StringUtils.equals(customField.getScene(), TemplateConstants.FieldTemplateScene.TEST_CASE.name())) {
                testCaseTemplateService.handleSystemFieldCreate(customFieldDao);
            } else if (StringUtils.equals(customField.getScene(), TemplateConstants.FieldTemplateScene.API.name())) {
                apiTemplateService.handleSystemFieldCreate(customFieldDao);
            } else {
                iIssueTemplateService.handleSystemFieldCreate(customFieldDao);
            }

        } else {
            checkExist(customField);
            mapper.update(customField);
        }
        return customField.getId();
    }

    @Override
    public List<CustomField> queryList(QueryCustomFieldRequest request) {
        QueryWrapper wrapper = buildWrapper(request);
        return mapper.selectListByQuery(wrapper);
    }

    @Override
    public List<String> listIds(QueryCustomFieldRequest request) {
        return null;
    }

    @Override
    public List<CustomField> getDefaultField(QueryCustomFieldRequest request) {
        List<CustomField> customFields = mapper.selectListByQuery(QueryWrapper.create()
                .where(CUSTOM_FIELD.SYSTEM.eq(true))
                .and(CUSTOM_FIELD.SCENE.eq(request.getScene()))
                .and(CUSTOM_FIELD.PROJECT_ID.eq(request.getProjectId())));
        List<String> fieldNames = customFields.stream().map(CustomField::getName).toList();
        List<CustomField> globalFields = getGlobalField(request.getScene());
        globalFields.forEach(item -> {
            if (!fieldNames.contains(item.getName())) {
                customFields.add(item);
            }
        });
        return customFields;
    }

    private List<CustomField> getGlobalField(String scene) {
        return mapper.selectListByQuery(QueryWrapper.create()
                .where(CUSTOM_FIELD.GLOBAL.eq(true)).and(CUSTOM_FIELD.SCENE.eq(scene)));
    }

    private void checkExist(CustomField customField) {
        if (Objects.nonNull(customField.getName())) {
            boolean exists = QueryChain.of(CustomField.class).where(CUSTOM_FIELD.NAME.eq(StringUtils.trim(customField.getName())))
                    .and(CUSTOM_FIELD.PROJECT_ID.eq(customField.getProjectId()))
                    .and(CUSTOM_FIELD.SCENE.eq(customField.getScene()))
                    .and(CUSTOM_FIELD.ID.ne(customField.getId())).exists();
            if (exists) {
                CustomException.throwException("工作空间下已存在该字段：" + customField.getName());
            }
        }
    }

}
