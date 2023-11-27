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

/**
 *  实体类。
 *
 * @author 11's papa
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "custom_field_template")
public class CustomFieldTemplate implements Serializable {

    /**
     * Custom field field template related id
     */
    @Id(keyType = KeyType.Generator, value = "flexId")
    private String id;

    /**
     * Custom field ID
     */
    private String fieldId;

    /**
     * Field template ID
     */
    private String templateId;

    /**
     * Use scene
     */
    private String scene;

    /**
     * Is required
     */
    private Boolean required;

    /**
     * Item order
     */
    private long order;

    private String defaultValue;

    /**
     * Custom data
     */
    private String customData;

    /**
     * Save Table Header Key
     */
    private String key;

    /**
     * delete flag
     */
    @Column(isLogicDelete = true)
    private Boolean delFlag;

}
