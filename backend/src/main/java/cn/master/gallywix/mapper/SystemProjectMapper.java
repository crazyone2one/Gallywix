package cn.master.gallywix.mapper;

import com.mybatisflex.core.BaseMapper;
import cn.master.gallywix.entity.SystemProject;
import org.apache.ibatis.annotations.Select;

/**
 * 项目信息 映射层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface SystemProjectMapper extends BaseMapper<SystemProject> {

    @Select("SELECT MAX(system_id) from tb_system_project")
    String getMaxSystemId();
}
