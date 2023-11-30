package cn.master.gallywix.service;

import cn.master.gallywix.controller.vo.BaseQueryVO;
import cn.master.gallywix.controller.vo.template.UpdateCaseFieldTemplateRequest;
import cn.master.gallywix.dto.CustomFieldDao;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import cn.master.gallywix.entity.TestCaseTemplate;

/**
 *  服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface ITestCaseTemplateService extends IService<TestCaseTemplate> {

    void handleSystemFieldCreate(CustomFieldDao customFieldDao);

    String add(UpdateCaseFieldTemplateRequest request);

    void update(UpdateCaseFieldTemplateRequest testCaseTemplate);

    Page<TestCaseTemplate> queryPage(BaseQueryVO<TestCaseTemplate> page);
}
