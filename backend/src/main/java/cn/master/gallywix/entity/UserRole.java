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
 * 实体类。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "tb_user_role")
public class UserRole implements Serializable {

    /**
     * ID of user's role info
     */
    @Id(keyType = KeyType.Generator, value = "flexId")
    private String id;

    /**
     * User ID of this user-role info
     */
    private String userId;

    /**
     * Role ID of this user-role info
     */
    private String roleId;

    /**
     * The (system|organization|workspace) ID of this user-role info
     */
    private String sourceId;

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
