package cn.master.gallywix.dto;

import cn.master.gallywix.entity.SystemGroup;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 11's papa
 * @since 11/16/2023
 **/
@Setter
@Getter
public class GroupDTO extends SystemGroup {
    private String scopeName;
    private Long memberSize;
}
