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
import cn.master.gallywix.entity.SystemRole;
import cn.master.gallywix.service.ISystemRoleService;
import org.springframework.web.bind.annotation.RestController;
import java.io.Serializable;
import java.util.List;

/**
 * 角色信息 控制层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@RestController
@RequestMapping("/systemRole")
public class SystemRoleController {

    @Autowired
    private ISystemRoleService iSystemRoleService;

    /**
     * 添加角色信息。
     *
     * @param systemRole 角色信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody SystemRole systemRole) {
        return iSystemRoleService.save(systemRole);
    }

    /**
     * 根据主键删除角色信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return iSystemRoleService.removeById(id);
    }

    /**
     * 根据主键更新角色信息。
     *
     * @param systemRole 角色信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody SystemRole systemRole) {
        return iSystemRoleService.updateById(systemRole);
    }

    /**
     * 查询所有角色信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<SystemRole> list() {
        return iSystemRoleService.list();
    }

    /**
     * 根据角色信息主键获取详细信息。
     *
     * @param id 角色信息主键
     * @return 角色信息详情
     */
    @GetMapping("getInfo/{id}")
    public SystemRole getInfo(@PathVariable Serializable id) {
        return iSystemRoleService.getById(id);
    }

    /**
     * 分页查询角色信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<SystemRole> page(Page<SystemRole> page) {
        return iSystemRoleService.page(page);
    }

}
