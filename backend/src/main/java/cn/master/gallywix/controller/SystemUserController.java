package cn.master.gallywix.controller;

import cn.master.gallywix.common.result.ResponseResult;
import cn.master.gallywix.dto.user.UserDTO;
import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.service.ISystemUserService;
import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息表 控制层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class SystemUserController {

    private final ISystemUserService iSystemUserService;

    /**
     * 添加用户信息表。
     *
     * @param systemUser 用户信息表
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public ResponseResult<UserDTO> save(@RequestBody SystemUser systemUser) {
        return ResponseResult.success(iSystemUserService.saveUser(systemUser));
    }

    /**
     * 根据主键删除用户信息表。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return iSystemUserService.removeById(id);
    }

    /**
     * 根据主键更新用户信息表。
     *
     * @param systemUser 用户信息表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody SystemUser systemUser) {
        return iSystemUserService.updateById(systemUser);
    }

    /**
     * 查询所有用户信息表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<SystemUser> list() {
        return iSystemUserService.list();
    }

    /**
     * 根据用户信息表主键获取详细信息。
     *
     * @param id 用户信息表主键
     * @return 用户信息表详情
     */
    @GetMapping("getInfo/{id}")
    public SystemUser getInfo(@PathVariable Serializable id) {
        return iSystemUserService.getById(id);
    }

    /**
     * 分页查询用户信息表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<SystemUser> page(Page<SystemUser> page) {
        return iSystemUserService.page(page);
    }

}
