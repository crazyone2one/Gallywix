package cn.master.gallywix.dto;

import cn.master.gallywix.entity.SystemProject;
import cn.master.gallywix.entity.SystemWorkspace;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 11's papa
 * @since 11/22/2023
 **/
@Data
public class WorkspaceResource {
    private List<SystemWorkspace> workspaces = new ArrayList<>();
    private List<SystemProject> projects = new ArrayList<>();
}
