package cn.master.gallywix.mapper;

import com.mybatisflex.core.BaseMapper;
import cn.master.gallywix.entity.SystemUser;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * 用户信息表 映射层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface SystemUserMapper extends BaseMapper<SystemUser> {
    @MapKey("id")
    Map<String, SystemUser> queryNameByIds(List<String> userIds);
}
