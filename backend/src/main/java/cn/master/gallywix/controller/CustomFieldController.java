package cn.master.gallywix.controller;

import cn.master.gallywix.controller.vo.QueryCustomFieldRequest;
import cn.master.gallywix.entity.CustomField;
import cn.master.gallywix.service.ICustomFieldService;
import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 控制层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@RestController
@RequestMapping("/custom/field")
@RequiredArgsConstructor
public class CustomFieldController {

    private final ICustomFieldService iCustomFieldService;

    /**
     * 添加。
     *
     * @param customField param
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public String save(@RequestBody CustomField customField) {
        return iCustomFieldService.add(customField);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return iCustomFieldService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param customField param
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public String update(@RequestBody CustomField customField) {
        return iCustomFieldService.updateCustomField(customField);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @PostMapping("/list")
    public List<CustomField> listIds(@RequestBody QueryCustomFieldRequest request) {
        return iCustomFieldService.queryList(request);
    }

    @PostMapping("/list/ids")
    public List<String> list(@RequestBody QueryCustomFieldRequest request) {
        return iCustomFieldService.listIds(request);
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public CustomField getInfo(@PathVariable Serializable id) {
        return iCustomFieldService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @PostMapping("page")
    public Page<CustomField> page(@RequestBody QueryCustomFieldRequest page) {
        return iCustomFieldService.queryPage(page);
    }

}
