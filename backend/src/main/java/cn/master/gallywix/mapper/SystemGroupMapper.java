package cn.master.gallywix.mapper;

import cn.master.gallywix.controller.vo.group.GroupPageReqVO;
import cn.master.gallywix.dto.GroupDTO;
import cn.master.gallywix.entity.SystemGroup;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import org.apache.ibatis.annotations.Param;

/**
 *  映射层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface SystemGroupMapper extends BaseMapper<SystemGroup> {
    Page<GroupDTO> getGroupList(@Param("request") GroupPageReqVO request);
}
