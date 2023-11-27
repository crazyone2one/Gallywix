package cn.master.gallywix.entity;

import cn.master.gallywix.listener.IssueTemplateListener;
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
@Table(value = "issue_template", onInsert = IssueTemplateListener.class, onUpdate = IssueTemplateListener.class)
public class IssueTemplate implements Serializable {

    @Id(keyType = KeyType.Generator, value = "flexId")
    private String id;

    /**
     * Field template name
     */
    private String name;

    /**
     * Field template type
     */
    private String platform;

    /**
     * Field template description
     */
    private String description;

    /**
     * Issue title
     */
    private String title;

    /**
     * Is system field template
     */
    private Boolean system;

    /**
     * Is global template
     */
    private Boolean global;

    /**
     * Issue content
     */
    private String content;

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

    private String projectId;

    private String createUser;

    private String updateUser;

}
