package cn.master.gallywix.controller;

import cn.master.gallywix.common.result.ResponseResult;
import cn.master.gallywix.controller.vo.project.ProjectPageReqVO;
import cn.master.gallywix.entity.SystemProject;
import cn.master.gallywix.service.ISystemProjectService;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 项目信息 控制层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@RestController
@RequestMapping("/system/project")
@RequiredArgsConstructor
public class SystemProjectController {

    private final ISystemProjectService iSystemProjectService;

    /**
     * 添加项目信息。
     *
     * @param systemProject 项目信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public ResponseResult<SystemProject> save(@RequestBody SystemProject systemProject) {
        return ResponseResult.success(iSystemProjectService.saveProject(systemProject));
    }

    /**
     * 根据主键删除项目信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public ResponseResult<Integer> remove(@PathVariable String id) {
        return ResponseResult.success(iSystemProjectService.deleteProject(id));
    }

    /**
     * 根据主键更新项目信息。
     *
     * @param systemProject 项目信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public ResponseResult<Boolean> update(@RequestBody SystemProject systemProject) {
        return ResponseResult.success(iSystemProjectService.updateById(systemProject));
    }

    @GetMapping("list")
    public ResponseResult<List<SystemProject>> list() {
        return ResponseResult.success(iSystemProjectService.list());
    }

    /**
     * 查询所有项目信息。
     *
     * @return 所有数据
     */
    @PostMapping("/list/related")
    public ResponseResult<List<SystemProject>> list(@RequestBody ProjectPageReqVO request) {
        request.setUserId(SessionUtils.getUserId());
        return ResponseResult.success(iSystemProjectService.getUserProject(request));
    }

    /**
     * 根据项目信息主键获取详细信息。
     *
     * @param id 项目信息主键
     * @return 项目信息详情
     */
    @GetMapping("getInfo/{id}")
    public ResponseResult<SystemProject> getInfo(@PathVariable Serializable id) {
        return ResponseResult.success(iSystemProjectService.getById(id));
    }

    /**
     * 分页查询项目信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @PostMapping("page")
//    @PreAuthorize("hasAnyAuthority('ADMIN1','project_query')")
    public ResponseResult<Page<SystemProject>> page(@RequestBody ProjectPageReqVO page) {
        return ResponseResult.success(iSystemProjectService.getProjectPageList(page));
    }
}
