package cn.master.gallywix.entity;

import cn.master.gallywix.listener.ApiTemplateListener;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * api模版表 实体类。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "api_template", onInsert = ApiTemplateListener.class, onUpdate = ApiTemplateListener.class)
public class ApiTemplate implements Serializable {

    @Id(keyType = KeyType.Generator, value = "flexId")
    private String id;

    /**
     * Field template name
     */
    private String name;

    /**
     * Field template description
     */
    private String description;

    /**
     * Is system field template
     */
    private Boolean system;

    /**
     * Is global template
     */
    private Boolean global;

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

    private String updateUser;

    private String projectId;

    @Column(isLogicDelete = true)
    private Boolean delFlag;

}
