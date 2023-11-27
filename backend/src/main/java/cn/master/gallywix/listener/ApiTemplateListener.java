package cn.master.gallywix.listener;

import cn.master.gallywix.entity.ApiTemplate;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;

/**
 * @author 11's papa
 * @since 11/27/2023
 **/
public class ApiTemplateListener implements InsertListener, UpdateListener {
    @Override
    public void onInsert(Object o) {
        ApiTemplate template = (ApiTemplate) o;
        template.setCreateUser(SessionUtils.getUserId());
        template.setUpdateUser(SessionUtils.getUserId());
    }

    @Override
    public void onUpdate(Object o) {
        ApiTemplate template = (ApiTemplate) o;
        template.setUpdateUser(SessionUtils.getUserId());
    }
}
