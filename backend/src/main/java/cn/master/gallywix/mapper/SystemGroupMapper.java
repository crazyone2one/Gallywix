package cn.master.gallywix.mapper;

import cn.master.gallywix.dto.GroupDTO;
import cn.master.gallywix.entity.SystemGroup;
import com.mybatisflex.core.BaseMapper;

/**
 * 映射层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface SystemGroupMapper extends BaseMapper<SystemGroup> {
    GroupDTO getGroupList();
}
