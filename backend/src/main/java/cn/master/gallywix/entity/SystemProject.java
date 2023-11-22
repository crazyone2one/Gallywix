package cn.master.gallywix.entity;

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
 * 项目信息 实体类。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_system_project")
public class SystemProject implements Serializable {

    /**
     * Project ID
     */
    @Id(keyType = KeyType.Generator, value = "flexId")
    private String id;

    /**
     * Workspace ID this project belongs to
     */
    private String workspaceId;

    /**
     * Project name
     */
    private String name;

    /**
     * Project description
     */
    private String description;

    private String systemId;

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

    @Column(ignore = true)
    private long memberSize;
}
