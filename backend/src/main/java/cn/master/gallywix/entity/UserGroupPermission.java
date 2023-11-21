package cn.master.gallywix.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
@Table(value = "tb_user_group_permission")
public class UserGroupPermission implements Serializable {

    @Id(keyType = KeyType.Generator, value = "flexId")
    private String id;

    /**
     * 用户组ID
     */
    private String groupId;

    private String permissionId;

    /**
     * 功能菜单
     */
    private String moduleId;

}
