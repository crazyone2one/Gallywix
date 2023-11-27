package cn.master.gallywix.listener;

import cn.master.gallywix.entity.TestCaseTemplate;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;

/**
 * @author 11's papa
 * @since 11/27/2023
 **/
public class TestCaseTemplateListener implements InsertListener, UpdateListener {
    @Override
    public void onInsert(Object o) {
        TestCaseTemplate template = (TestCaseTemplate) o;
        template.setCreateUser(SessionUtils.getUserId());
        template.setUpdateUser(SessionUtils.getUserId());
    }

    @Override
    public void onUpdate(Object o) {
        TestCaseTemplate template = (TestCaseTemplate) o;
        template.setUpdateUser(SessionUtils.getUserId());
    }
}
