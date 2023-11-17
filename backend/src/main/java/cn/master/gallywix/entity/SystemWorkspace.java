package cn.master.gallywix.entity;

import cn.master.gallywix.listener.workspace.WorkspaceListener;
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
 * 工作空间信息 实体类。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_system_workspace", onInsert = WorkspaceListener.class, onUpdate = WorkspaceListener.class)
public class SystemWorkspace implements Serializable {

    /**
     * Workspace ID
     */
    @Id(keyType = KeyType.Generator, value = "flexId")
    private String id;

    /**
     * Organization ID this workspace belongs to
     */
    private String organizationId;

    /**
     * Workspace name
     */
    private String name;

    /**
     * Workspace description
     */
    private String description;

    private String createUser;
    private String updateUser;

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

    /**
     * delete flag
     */
    @Column(isLogicDelete = true)
    private Boolean delFlag;

}
