package cn.master.gallywix.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 *  实体类。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "custom_field")
public class CustomField implements Serializable {

    /**
     * Custom field ID
     */
    @Id(keyType = KeyType.Generator, value = "flexId")
    private String id;

    /**
     * Custom field name
     */
    private String name;

    /**
     * Custom field use scene
     */
    private String scene;

    /**
     * Custom field type
     */
    private String type;

    /**
     * Custom field remark
     */
    private String remark;

    /**
     * Test resource pool status
     */
    @Column(typeHandler = JacksonTypeHandler.class)
    private List<Map<String, Object>> options;

    /**
     * Is system custom field
     */
    private Boolean system;

    /**
     * Is global custom field
     */
    private Boolean global;

    /**
     * Workspace ID this custom field belongs to
     */
    private String workspaceId;

    /**
     * Create timestamp
     */
    @Column(onInsertValue = "now()")
    private LocalDateTime createTime;

    /**
     * Update timestamp
     */
    @Column(onInsertValue = "now()", onUpdateValue = "now()")
    private LocalDateTime updateTime;

    private String createUser;

    private String projectId;

    private Boolean thirdPart;
    /**
     * delete flag
     */
    @Column(isLogicDelete = true)
    private Boolean delFlag;

}
