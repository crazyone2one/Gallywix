package cn.master.gallywix.controller;

import cn.master.gallywix.controller.vo.BaseQueryVO;
import cn.master.gallywix.controller.vo.template.UpdateCaseFieldTemplateRequest;
import cn.master.gallywix.entity.TestCaseTemplate;
import cn.master.gallywix.service.ITestCaseTemplateService;
import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 *  控制层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@RestController
@RequestMapping("/field/template/case")
@RequiredArgsConstructor
public class TestCaseTemplateController {
    private final ITestCaseTemplateService iTestCaseTemplateService;

    /**
     * 添加。
     *
     * @param request  param
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public String save(@RequestBody UpdateCaseFieldTemplateRequest request) {
        return iTestCaseTemplateService.add(request);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return iTestCaseTemplateService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param testCaseTemplate 
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public void update(@RequestBody UpdateCaseFieldTemplateRequest testCaseTemplate) {
        iTestCaseTemplateService.update(testCaseTemplate);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<TestCaseTemplate> list() {
        return iTestCaseTemplateService.list();
    }
    @GetMapping({"/option/{projectId}", "/option"})
    public List<TestCaseTemplate> getFieldTemplateOption(@PathVariable(required = false) String projectId) {
        return iTestCaseTemplateService.getOption(projectId);
    }
    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public TestCaseTemplate getInfo(@PathVariable Serializable id) {
        return iTestCaseTemplateService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @PostMapping("page")
    public Page<TestCaseTemplate> page(@RequestBody BaseQueryVO<TestCaseTemplate> page) {
        return iTestCaseTemplateService.queryPage(page);
    }

}
