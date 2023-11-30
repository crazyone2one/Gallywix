package cn.master.gallywix.entity;

import cn.master.gallywix.listener.TestCaseTemplateListener;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
@Table(value = "test_case_template", onInsert = TestCaseTemplateListener.class, onUpdate = TestCaseTemplateListener.class)
public class TestCaseTemplate implements Serializable {

    @Id(keyType = KeyType.Generator, value = "flexId")
    private String id;

    /**
     * Field template name
     */
    private String name;

    /**
     * Field template type
     */
    private String type;

    /**
     * Field template description
     */
    private String description;

    /**
     * Test Case Name
     */
    private String caseName;

    /**
     * Is system field template
     */
    private Boolean system;

    /**
     * Is global template
     */
    private Boolean global;

    /**
     * Test case prerequisite
     */
    private String prerequisite;

    /**
     * Test case steps desc
     */
    private String stepDescription;

    /**
     * Test case expected result
     */
    private String expectedResult;

    /**
     * Test case actual result
     */
    private String actualResult;

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

    private String projectId;

    private String workspaceId;

    /**
     * Step model
     */
    private String stepModel;

    /**
     * Test case step
     */
    @Column(typeHandler = JacksonTypeHandler.class)
    private List<Map<String, Object>> steps;

    private String createUser;

    private String updateUser;
    /**
     * delete flag
     */
    @Column(isLogicDelete = true)
    private Boolean delFlag;
}
