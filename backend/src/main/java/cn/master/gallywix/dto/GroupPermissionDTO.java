package cn.master.gallywix.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 11's papa
 * @since 11/21/2023
 **/
@Data
public class GroupPermissionDTO {
    private List<GroupResourceDTO> permissions = new ArrayList<>();
}
