package cn.master.gallywix.controller;

import cn.master.gallywix.common.result.ResponseResult;
import cn.master.gallywix.controller.vo.group.EditGroupRequest;
import cn.master.gallywix.controller.vo.group.GroupPageReqVO;
import cn.master.gallywix.controller.vo.group.GroupRequest.GroupRequest;
import cn.master.gallywix.controller.vo.workspace.AddMemberRequest.AddMemberRequest;
import cn.master.gallywix.dto.GroupDTO;
import cn.master.gallywix.entity.SystemGroup;
import cn.master.gallywix.service.ISystemGroupService;
import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 控制层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@RestController
@RequestMapping("/system/group")
@RequiredArgsConstructor
public class SystemGroupController {

    private final ISystemGroupService iSystemGroupService;

    /**
     * 添加。
     *
     * @param request group param
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public ResponseResult<SystemGroup> save(@RequestBody EditGroupRequest request) {
        return ResponseResult.success(iSystemGroupService.addGroup(request));
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public ResponseResult<String> remove(@PathVariable Serializable id) {
        return ResponseResult.success(iSystemGroupService.deleteGroup(id));
    }

    /**
     * 根据主键更新。
     *
     * @param request group param
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public ResponseResult<SystemGroup> update(@RequestBody EditGroupRequest request) {
        return ResponseResult.success(iSystemGroupService.editGroup(request));
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @PostMapping("/list")
    public ResponseResult<List<SystemGroup>> list(@RequestBody GroupRequest request) {
        return ResponseResult.success(iSystemGroupService.getGroupsByType(request));
    }

    @PostMapping("/list-by-type")
    public ResponseResult<List<SystemGroup>> getGroupByType(@RequestBody EditGroupRequest request) {
        return ResponseResult.success(iSystemGroupService.getGroupByType(request));
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public SystemGroup getInfo(@PathVariable Serializable id) {
        return iSystemGroupService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @PostMapping("page")
    public ResponseResult<Page<GroupDTO>> page(@RequestBody GroupPageReqVO page) {
        return ResponseResult.success(iSystemGroupService.getGroupPageList(page));
    }

    @GetMapping("/all/{userId}")
    public ResponseResult<List<Map<String, Object>>> getAllUserGroup(@PathVariable("userId") String userId) {
        return ResponseResult.success(iSystemGroupService.getAllUserGroup(userId));
    }

    @PostMapping("/special/ws/member/add")
    public ResponseResult<String> addMemberByAdmin(@RequestBody AddMemberRequest request) {
        return ResponseResult.success(iSystemGroupService.addMember(request));
    }

    @PostMapping("/permission/edit")
    public ResponseResult<String> editGroupPermission(@RequestBody EditGroupRequest editGroupRequest) {
        return ResponseResult.success(iSystemGroupService.editGroupPermission(editGroupRequest));
    }
}
