package cn.master.gallywix.entity;

import cn.master.gallywix.listener.group.SystemGroupListener;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(value = "tb_system_group", onInsert = SystemGroupListener.class)
public class SystemGroup implements Serializable {

    @Id(keyType = KeyType.Generator, value = "flexId")
    private String id;

    private String name;
    private String code;

    private String description;

    /**
     * 是否是系统用户组
     */
    private Boolean system;

    /**
     * 所属类型
     */
    private String type;
    @Column(onInsertValue = "now()")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Column(onInsertValue = "now()", onUpdateValue = "now()")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 创建人(操作人）
     */
    private String creator;

    /**
     * 应用范围
     */
    private String scopeId;

    /**
     * delete flag
     */
    @Column(isLogicDelete = true)
    private Boolean delFlag;

}
