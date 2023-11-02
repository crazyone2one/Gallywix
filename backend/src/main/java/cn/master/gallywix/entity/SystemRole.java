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
 * 角色信息 实体类。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_system_role")
public class SystemRole implements Serializable {

    /**
     * Role ID
     */
    @Id(keyType = KeyType.Generator, value = "flexId")
    private String id;

    /**
     * Role name
     */
    private String name;

    /**
     * Role description
     */
    private String description;

    /**
     * Role type, (system|organization|workspace)
     */
    private String type;

    /**
     * （0正常 1删除）
     */
    @Column(isLogicDelete = true)
    private Boolean delFlag;

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

}
