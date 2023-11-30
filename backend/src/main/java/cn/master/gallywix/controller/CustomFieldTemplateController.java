package cn.master.gallywix.controller;

import cn.master.gallywix.dto.CustomFieldTemplateDao;
import cn.master.gallywix.entity.CustomFieldTemplate;
import cn.master.gallywix.service.ICustomFieldTemplateService;
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
@RequestMapping("/custom/field/template")
@RequiredArgsConstructor
public class CustomFieldTemplateController {
    private final ICustomFieldTemplateService iCustomFieldTemplateService;

    /**
     * 添加。
     *
     * @param customFieldTemplate 
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody CustomFieldTemplate customFieldTemplate) {
        return iCustomFieldTemplateService.save(customFieldTemplate);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return iCustomFieldTemplateService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param customFieldTemplate 
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody CustomFieldTemplate customFieldTemplate) {
        return iCustomFieldTemplateService.updateById(customFieldTemplate);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @PostMapping("list")
    public List<CustomFieldTemplateDao> list(@RequestBody CustomFieldTemplate request) {
        return iCustomFieldTemplateService.list(request);
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public CustomFieldTemplate getInfo(@PathVariable Serializable id) {
        return iCustomFieldTemplateService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<CustomFieldTemplate> page(Page<CustomFieldTemplate> page) {
        return iCustomFieldTemplateService.page(page);
    }

}
