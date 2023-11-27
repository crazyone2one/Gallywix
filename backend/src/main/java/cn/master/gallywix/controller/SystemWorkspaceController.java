package cn.master.gallywix.controller;

import cn.master.gallywix.common.result.ResponseResult;
import cn.master.gallywix.controller.vo.workspace.WorkspacePageReqVO;
import cn.master.gallywix.dto.WorkspaceResource;
import cn.master.gallywix.entity.SystemWorkspace;
import cn.master.gallywix.service.ISystemWorkspaceService;
import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 工作空间信息 控制层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@RestController
@RequestMapping("/workspace")
@RequiredArgsConstructor
public class SystemWorkspaceController {

    private final ISystemWorkspaceService iSystemWorkspaceService;

    /**
     * 添加工作空间信息。
     *
     * @param systemWorkspace 工作空间信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public ResponseResult<SystemWorkspace> save(@RequestBody SystemWorkspace systemWorkspace) {
        return ResponseResult.success(iSystemWorkspaceService.add(systemWorkspace));
    }

    /**
     * 根据主键删除工作空间信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable String id) {
        return ResponseResult.success(iSystemWorkspaceService.deleteWorkspace(id));
    }

    /**
     * 根据主键更新工作空间信息。
     *
     * @param systemWorkspace 工作空间信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody SystemWorkspace systemWorkspace) {
        return iSystemWorkspaceService.updateById(systemWorkspace);
    }

    /**
     * 查询所有工作空间信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public ResponseResult<List<SystemWorkspace>> list() {
        return ResponseResult.success(iSystemWorkspaceService.list());
    }

    /**
     * 根据工作空间信息主键获取详细信息。
     *
     * @param id 工作空间信息主键
     * @return 工作空间信息详情
     */
    @GetMapping("getInfo/{id}")
    public SystemWorkspace getInfo(@PathVariable Serializable id) {
        return iSystemWorkspaceService.getById(id);
    }

    /**
     * 分页查询工作空间信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @PostMapping("page")
    public Page<SystemWorkspace> page(@RequestBody WorkspacePageReqVO page) {
        return iSystemWorkspaceService.findDataByPage(page);
    }

    @GetMapping("/list/resource/{groupId}/{type}")
    public ResponseResult<WorkspaceResource> listResource(@PathVariable String groupId, @PathVariable String type) {
        return ResponseResult.success(iSystemWorkspaceService.listResource(groupId, type));
    }
}
