package cn.master.gallywix.controller;

import cn.master.gallywix.common.result.ResponseResult;
import cn.master.gallywix.controller.vo.organization.OrganizationReqVO;
import cn.master.gallywix.entity.SystemOrganization;
import cn.master.gallywix.service.ISystemOrganizationService;
import com.mybatisflex.core.paginate.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 组织信息 控制层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@RestController
@RequestMapping("/system/organization")
@RequiredArgsConstructor
public class SystemOrganizationController {

    private final ISystemOrganizationService iSystemOrganizationService;

    /**
     * 添加组织信息。
     *
     * @param systemOrganization 组织信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public ResponseResult<SystemOrganization> save(@RequestBody SystemOrganization systemOrganization) {
        return ResponseResult.success(iSystemOrganizationService.addOrganization(systemOrganization));
    }

    /**
     * 根据主键删除组织信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable Serializable id) {
        return ResponseResult.success(iSystemOrganizationService.removeById(id));
    }

    /**
     * 根据主键更新组织信息。
     *
     * @param systemOrganization 组织信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public ResponseResult<Boolean> update(@RequestBody SystemOrganization systemOrganization) {
        return ResponseResult.success(iSystemOrganizationService.updateById(systemOrganization));
    }

    /**
     * 查询所有组织信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public ResponseResult<List<SystemOrganization>> list() {
        return ResponseResult.success(iSystemOrganizationService.list());
    }

    /**
     * 根据组织信息主键获取详细信息。
     *
     * @param id 组织信息主键
     * @return 组织信息详情
     */
    @GetMapping("getInfo/{id}")
    public ResponseResult<SystemOrganization> getInfo(@PathVariable Serializable id) {
        return ResponseResult.success(iSystemOrganizationService.getById(id));
    }

    /**
     * 分页查询组织信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public ResponseResult<Page<SystemOrganization>> page(OrganizationReqVO page) {
        return ResponseResult.success(iSystemOrganizationService.findDataByPage(page));
    }

}
