package cn.master.gallywix.entity;

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

    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    private Boolean status;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * （0正常 1删除）
     */
    private Boolean delFlag;

    private String language;

    private String lastWorkspaceId;

    private String lastOrganizationId;

    /**
     * Phone number of user
     */
    private String phone;

}
