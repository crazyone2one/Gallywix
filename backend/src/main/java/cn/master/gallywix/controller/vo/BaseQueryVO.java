package cn.master.gallywix.controller.vo;

import cn.master.gallywix.utils.JsonUtils;
import com.mybatisflex.core.paginate.Page;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author 11's papa
 * @since 11/27/2023
 **/
@Setter
@Getter
public class BaseQueryVO<T> extends Page<T> {
    private String projectId;

    private String name;

    /**
     * 状态不等于 notEqStatus
     */
    private String notEqStatus;

    private String workspaceId;

    private List<String> ids;

    private List<String> moduleIds;

    private List<String> nodeIds;

    /**
     * 排除哪些id
     */
    private List<String> notInIds;

    /**
     * 是否选中所有数据
     */
    private boolean selectAll;

    /**
     * 全选之后取消选中的id
     */
    private List<String> unSelectIds;

    /**
     * 排序条件
     */
    private List<OrderRequest> orders;

    /**
     * 过滤条件
     */
    private Map<String, List<String>> filters;

    /**
     * 高级搜索
     */
    private Map<String, Object> combine;

    /**
     * 版本 ID
     */
    private String versionId;

    /**
     * 版本来源字段
     */
    private String refId;

    /**
     * 测试计划关联场景过滤掉步骤为0的场景
     */
    private boolean hasStep;

    private String scenarioType;

    public Map<String, List<String>> getFilters() {
        if (MapUtils.isEmpty(filters) || filters.containsKey("isHandleCustomMultiple")) {
            return filters;
        }
        // 处理过滤器中的自定义字段多选值
        filters.forEach((k,v) -> {
            if (k.contains("custom_multiple") && CollectionUtils.isNotEmpty(v)) {
                filters.put(k, Collections.singletonList(JsonUtils.toJsonPrettyString(v)));
            }
        });
        filters.put("isHandleCustomMultiple", null);
        return filters;
    }
}
