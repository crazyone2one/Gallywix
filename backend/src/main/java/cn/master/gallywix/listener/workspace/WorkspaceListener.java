package cn.master.gallywix.listener.workspace;

import cn.master.gallywix.entity.SystemWorkspace;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;

/**
 * @author 11's papa
 * @since 11/17/2023
 **/
public class WorkspaceListener implements InsertListener, UpdateListener {
    @Override
    public void onInsert(Object o) {
        SystemWorkspace workspace = (SystemWorkspace) o;
        workspace.setCreateUser(SessionUtils.sessionUserInfo().getUsername());
        workspace.setUpdateUser(SessionUtils.sessionUserInfo().getUsername());
    }

    @Override
    public void onUpdate(Object o) {
        SystemWorkspace workspace = (SystemWorkspace) o;
        workspace.setUpdateUser(SessionUtils.sessionUserInfo().getUsername());
    }
}
