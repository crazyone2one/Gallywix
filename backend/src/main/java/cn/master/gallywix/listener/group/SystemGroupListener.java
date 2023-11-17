package cn.master.gallywix.listener.group;

import cn.master.gallywix.entity.SystemGroup;
import cn.master.gallywix.utils.SessionUtils;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;

/**
 * @author 11's papa
 * @since 11/16/2023
 **/
public class SystemGroupListener implements InsertListener, UpdateListener {
    @Override
    public void onInsert(Object o) {
        SystemGroup systemGroup = (SystemGroup) o;
        systemGroup.setCreator(SessionUtils.sessionUserInfo().getUsername());
    }

    @Override
    public void onUpdate(Object o) {

    }
}
