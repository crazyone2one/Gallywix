package cn.master.gallywix.utils;

import cn.master.gallywix.controller.vo.OrderRequest;
import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.service.ISystemUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author 11's papa
 * @since 11/16/2023
 **/
public class ServiceUtils {
    public static final int ORDER_STEP = 5000;

    public static List<OrderRequest> getDefaultOrder(List<OrderRequest> orders) {
        return getDefaultOrder(null, orders);
    }

    public static List<OrderRequest> getDefaultOrder(String prefix, List<OrderRequest> orders) {
        return getDefaultOrderByField(prefix, orders, "update_time");
    }

    public static List<OrderRequest> getDefaultOrderByField(List<OrderRequest> orders, String field) {
        return getDefaultOrderByField(null, orders, field);
    }

    private static List<OrderRequest> getDefaultOrderByField(String prefix, List<OrderRequest> orders, String field) {
        if (Objects.isNull(orders) || CollectionUtils.isEmpty(orders)) {
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setName(field);
            orderRequest.setType("desc");
            if (StringUtils.isNotBlank(prefix)) {
                orderRequest.setPrefix(prefix);
            }
            orders = new ArrayList<>();
            orders.add(orderRequest);
            return orders;
        }
        return orders;
    }

    public static Map<String, SystemUser> getUserMap(List<String> userIds) {
        ISystemUserService systemUserService = CommonBeanFactory.getBean(ISystemUserService.class);
        if (CollectionUtils.isNotEmpty(userIds)) {
            assert systemUserService != null;
            return systemUserService.queryNameByIds(userIds);
        }
        return new HashMap<>();
    }

    public static Map<String, String> getUserNameMap(List<String> userIds) {
        Map<String, SystemUser> userMap = getUserMap(userIds);
        HashMap<String, String> nameMap = new HashMap<>();
        userMap.forEach((k, v) -> {
            nameMap.put(k, v.getUsername());
        });
        return nameMap;
    }
}
