package cn.master.gallywix.controller;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import cn.master.gallywix.entity.ApiTemplate;
import cn.master.gallywix.service.IApiTemplateService;
import org.springframework.web.bind.annotation.RestController;
import java.io.Serializable;
import java.util.List;

/**
 * api模版表 控制层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@RestController
@RequestMapping("/apiTemplate")
public class ApiTemplateController {

    @Autowired
    private IApiTemplateService iApiTemplateService;

    /**
     * 添加api模版表。
     *
     * @param apiTemplate api模版表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody ApiTemplate apiTemplate) {
        return iApiTemplateService.save(apiTemplate);
    }

    /**
     * 根据主键删除api模版表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return iApiTemplateService.removeById(id);
    }

    /**
     * 根据主键更新api模版表。
     *
     * @param apiTemplate api模版表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody ApiTemplate apiTemplate) {
        return iApiTemplateService.updateById(apiTemplate);
    }

    /**
     * 查询所有api模版表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<ApiTemplate> list() {
        return iApiTemplateService.list();
    }

    /**
     * 根据api模版表主键获取详细信息。
     *
     * @param id api模版表主键
     * @return api模版表详情
     */
    @GetMapping("getInfo/{id}")
    public ApiTemplate getInfo(@PathVariable Serializable id) {
        return iApiTemplateService.getById(id);
    }

    /**
     * 分页查询api模版表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<ApiTemplate> page(Page<ApiTemplate> page) {
        return iApiTemplateService.page(page);
    }

}
