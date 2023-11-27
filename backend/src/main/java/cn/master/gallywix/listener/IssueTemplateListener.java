package cn.master.gallywix.listener;

import cn.master.gallywix.entity.IssueTemplate;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;

/**
 * @author 11's papa
 * @since 11/27/2023
 **/
public class IssueTemplateListener implements InsertListener, UpdateListener {
    @Override
    public void onInsert(Object o) {
        IssueTemplate template = (IssueTemplate) o;
        template.setCreateUser(SessionUtils.getUserId());
        template.setUpdateUser(SessionUtils.getUserId());
    }

    @Override
    public void onUpdate(Object o) {
        IssueTemplate template = (IssueTemplate) o;
        template.setUpdateUser(SessionUtils.getUserId());
    }
}
