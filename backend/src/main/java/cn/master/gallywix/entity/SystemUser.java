package cn.master.gallywix.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息表 实体类。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data(staticConstructor = "create")
@Table(value = "tb_system_user")
public class SystemUser implements Serializable {

    /**
     * User ID
     */
    @Id(keyType = KeyType.Generator, value = "flexId")
    private String id;

    /**
     * User name
     */
    private String username;

    /**
     * User name
     */
    private String nickname;

    /**
     * E-Mail address
     */
    private String email;

//    @JsonIgnore
    private String password;

    /**
     * 帐号状态（1正常 0停用）
     */
    private Boolean status;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    @Column(onInsertValue = "now()")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    @Column(onInsertValue = "now()", onUpdateValue = "now()")
    private LocalDateTime updateTime;

    /**
     * （0正常 1删除）
     */
    @Column(isLogicDelete = true)
    private Boolean delFlag;

    private String language;

    private String lastWorkspaceId;

    private String lastProjectId;

    /**
     * Phone number of user
     */
    private String phone;

}
