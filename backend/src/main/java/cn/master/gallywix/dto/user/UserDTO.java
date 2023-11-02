package cn.master.gallywix.dto.user;

import cn.master.gallywix.entity.SystemRole;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 11's papa
 * @since 11/02/2023
 **/
@Setter
@Getter
public class UserDTO {
    private String id;

    private String name;

    private String email;

    private String phone;

    private String status;

    private Long createTime;

    private Long updateTime;

    private List<SystemRole> roles = new ArrayList<>();
}
