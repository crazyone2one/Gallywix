package cn.master.gallywix.mapper;

import com.mybatisflex.core.BaseMapper;
import cn.master.gallywix.entity.CustomFieldTemplate;
import org.apache.ibatis.annotations.Param;

/**
 *  映射层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface CustomFieldTemplateMapper extends BaseMapper<CustomFieldTemplate> {
    Long getLastOrder(@Param("templateId") String templateId, @Param("baseOrder") Long baseOrder);
}
