package cn.master.gallywix.service.impl;

import cn.master.gallywix.common.constants.UserGroupConstants;
import cn.master.gallywix.controller.vo.group.EditGroupRequest;
import cn.master.gallywix.dto.*;
import cn.master.gallywix.entity.SystemGroup;
import cn.master.gallywix.entity.UserGroupPermission;
import cn.master.gallywix.mapper.UserGroupPermissionMapper;
import cn.master.gallywix.service.IUserGroupPermissionService;
import cn.master.gallywix.utils.JsonUtils;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.master.gallywix.entity.table.UserGroupPermissionTableDef.USER_GROUP_PERMISSION;

/**
 * 服务层实现。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserGroupPermissionServiceImpl extends ServiceImpl<UserGroupPermissionMapper, UserGroupPermission> implements IUserGroupPermissionService {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String editGroupPermission(EditGroupRequest request) {
        // 超级用户组禁止修改权限
        if (!StringUtils.equals(request.getUserGroupId(), UserGroupConstants.SUPER_GROUP)) {
            List<GroupPermission> permissions = request.getPermissions();
            if (CollectionUtils.isNotEmpty(permissions)) {
                QueryWrapper deleteWrapper = QueryWrapper.create()
                        .where(USER_GROUP_PERMISSION.GROUP_ID.eq(request.getUserGroupId()));
                mapper.deleteByQuery(deleteWrapper);
                String groupId = request.getUserGroupId();
                permissions.forEach(permission -> {
                    if (BooleanUtils.isTrue(permission.getChecked())) {
                        String permissionId = permission.getId();
                        String resourceId = permission.getResourceId();
                        UserGroupPermission build = UserGroupPermission.builder().groupId(groupId).permissionId(permissionId).moduleId(resourceId).build();
                        mapper.insert(build);
                    }
                });
            }
        }
        return "success";
    }

    @Override
    public GroupPermissionDTO getGroupResource(SystemGroup group) {
        GroupPermissionDTO dto = new GroupPermissionDTO();
        List<UserGroupPermission> groupPermissions = QueryChain.of(UserGroupPermission.class).where(USER_GROUP_PERMISSION.GROUP_ID.eq(group.getCode())).list();
        List<String> groupPermissionIds = groupPermissions.stream().map(UserGroupPermission::getPermissionId).toList();
        GroupJson groupJson = loadPermissionJsonFromService();

        List<GroupResourceDTO> dtoPermissions = dto.getPermissions();
        List<GroupResource> resource = groupJson.getResource();
        List<GroupPermission> permissions = groupJson.getPermissions();
        dtoPermissions.addAll(Objects.requireNonNull(getResourcePermission(resource, permissions, group, groupPermissionIds)));
        return dto;
    }

    private GroupJson loadPermissionJsonFromService() {
        GroupJson groupJson;
        List<GroupResource> globalResources = new ArrayList<>();
        Object obj = stringRedisTemplate.opsForHash().get("GA_PERMISSION", "service");
        GroupJson microServiceGroupJson = JsonUtils.parseObject((String) obj, GroupJson.class);
        assert microServiceGroupJson != null;
        List<GroupResource> globalResource = microServiceGroupJson.getResource().stream().filter(gp -> BooleanUtils.isTrue(gp.isGlobal())).toList();
        if (CollectionUtils.isNotEmpty(globalResource)) {
            globalResources.addAll(globalResource);
            microServiceGroupJson.getResource().removeIf(gp -> BooleanUtils.isTrue(gp.isGlobal()));
        }
        groupJson = microServiceGroupJson;
        // 拼装时通用权限Resource放在最后
        if (!globalResources.isEmpty()) {
            groupJson.getResource().addAll(globalResources);
        }
        return groupJson;
    }

    private List<GroupResourceDTO> getResourcePermission(List<GroupResource> resources, List<GroupPermission> permissions, SystemGroup group, List<String> permissionList) {
        List<GroupResourceDTO> dto = new ArrayList<>();
        List<GroupResource> grs;
        if (StringUtils.equals(group.getId(), UserGroupConstants.SUPER_GROUP)) {
            grs = resources;
            permissions.forEach(p -> p.setChecked(true));
        } else {
            grs = resources
                    .stream()
                    .filter(g -> g.getId().startsWith(group.getType()) || BooleanUtils.isTrue(g.isGlobal()))
                    .collect(Collectors.toList());
            permissions.forEach(p -> {
                if (permissionList.contains(p.getId())) {
                    p.setChecked(true);
                }
            });
        }
        for (GroupResource r : grs) {
            GroupResourceDTO resourceDTO = new GroupResourceDTO();
            resourceDTO.setResource(r);
            List<GroupPermission> collect = permissions
                    .stream()
                    .filter(p -> StringUtils.equals(r.getId(), p.getResourceId()))
                    .collect(Collectors.toList());
            resourceDTO.setPermissions(collect);
            resourceDTO.setType(r.getId().split("_")[0]);
            dto.add(resourceDTO);
        }
        return dto;
    }
}
