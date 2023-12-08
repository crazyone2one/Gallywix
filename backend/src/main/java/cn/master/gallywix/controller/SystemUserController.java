package cn.master.gallywix.controller;

import cn.master.gallywix.common.result.ResponseResult;
import cn.master.gallywix.controller.vo.user.AddOrgMemberRequestVO;
import cn.master.gallywix.controller.vo.user.UserPageReqVO;
import cn.master.gallywix.controller.vo.user.UserPasswordVO;
import cn.master.gallywix.dto.UserGroupPermissionDTO;
import cn.master.gallywix.dto.user.UserDTO;
import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.service.ISystemUserService;
import cn.master.gallywix.utils.SessionUtils;
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
    public ResponseResult<Boolean> remove(@PathVariable Serializable id) {
        return ResponseResult.success(iSystemUserService.removeById(id));
    }

    /**
     * 根据主键更新用户信息表。
     *
     * @param systemUser 用户信息表
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public ResponseResult<SystemUser> update(@RequestBody SystemUser systemUser) {
        return ResponseResult.success(iSystemUserService.updateUser(systemUser));
    }

    /**
     * 查询所有用户信息表。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public ResponseResult<List<SystemUser>> list() {
        return ResponseResult.success(iSystemUserService.list());
    }

    /**
     * 根据用户信息表主键获取详细信息。
     *
     * @param id 用户信息表主键
     * @return 用户信息表详情
     */
    @GetMapping("getInfo/{id}")
    public ResponseResult<SystemUser> getInfo(@PathVariable Serializable id) {
        return ResponseResult.success(iSystemUserService.getById(id));
    }

    /**
     * 分页查询用户信息表。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @PostMapping("page")
    public ResponseResult<Page<SystemUser>> page(@RequestBody UserPageReqVO page) {
        return ResponseResult.success(iSystemUserService.findDataByPage(page));
    }

    @PostMapping("/special/ws/member/page")
    public ResponseResult<Page<SystemUser>> getWsMemberUserPage(@RequestBody UserPageReqVO page) {
        return ResponseResult.success(iSystemUserService.findWsMemberByPage(page));
    }

    @PostMapping("/orgmember/add")
    public void addOrganizationMember(@RequestBody AddOrgMemberRequestVO request) {
        iSystemUserService.addOrganizationMember(request);
    }

    @GetMapping("/orgmember/delete/{organizationId}/{userId}")
    public void delOrganizationMember(@PathVariable String organizationId, @PathVariable String userId) {
        iSystemUserService.delOrganizationMember(organizationId, userId);
    }

    @GetMapping("/switch/source/{sign}/{sourceId}")
    public ResponseResult<SystemUser> switchUserRole(@PathVariable String sign, @PathVariable(value = "sourceId") String sourceId) {
        iSystemUserService.switchUserResource(sign, sourceId);
        return ResponseResult.success(SessionUtils.sessionUserInfo());
    }

    @PostMapping("/special/ws/member/list/all")
    public ResponseResult<List<SystemUser>> getMemberListByAdmin(@RequestBody UserPageReqVO request) {
        return ResponseResult.success(iSystemUserService.getMemberList(request));
    }

    @PostMapping("/ws/project/member/list")
    public ResponseResult<Page<SystemUser>> getProjectMemberListForWorkspace(@RequestBody UserPageReqVO request) {
        return ResponseResult.success(iSystemUserService.getProjectMemberList(request));
    }

    @GetMapping("/special/user/group/{userId}")
    public ResponseResult<UserGroupPermissionDTO> getUserGroup(@PathVariable("userId") String userId) {
        return ResponseResult.success(iSystemUserService.getUserGroup(userId));
    }

    @PostMapping("/special/password")
    public Boolean updatePassword(@RequestBody UserPasswordVO request) {
        return iSystemUserService.updateUserPassword(request);
    }
}
