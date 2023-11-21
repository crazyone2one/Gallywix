package cn.master.gallywix.controller;

import cn.master.gallywix.common.result.ResponseResult;
import cn.master.gallywix.controller.vo.group.EditGroupRequest;
import cn.master.gallywix.dto.GroupPermissionDTO;
import cn.master.gallywix.entity.SystemGroup;
import cn.master.gallywix.entity.UserGroupPermission;
import cn.master.gallywix.service.IUserGroupPermissionService;
import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 *  控制层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@RestController
@RequestMapping("/user/group")
@RequiredArgsConstructor
public class UserGroupPermissionController {

    private final IUserGroupPermissionService iUserGroupPermissionService;

    /**
     * 添加。
     *
     * @param userGroupPermission 
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody UserGroupPermission userGroupPermission) {
        return iUserGroupPermissionService.save(userGroupPermission);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return iUserGroupPermissionService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param editGroupRequest params
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("permission/update")
    public ResponseResult<String> update(@RequestBody EditGroupRequest editGroupRequest) {
        return ResponseResult.success(iUserGroupPermissionService.editGroupPermission(editGroupRequest));
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @PostMapping("/permission/list")
    public ResponseResult<GroupPermissionDTO> getGroupResource(@RequestBody SystemGroup group) {
        return ResponseResult.success(iUserGroupPermissionService.getGroupResource(group));
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public UserGroupPermission getInfo(@PathVariable Serializable id) {
        return iUserGroupPermissionService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<UserGroupPermission> page(Page<UserGroupPermission> page) {
        return iUserGroupPermissionService.page(page);
    }

}
