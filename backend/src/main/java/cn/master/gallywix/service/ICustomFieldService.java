package cn.master.gallywix.service;

import cn.master.gallywix.controller.vo.QueryCustomFieldRequest;
import cn.master.gallywix.entity.CustomField;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 *  服务层。
 *
 * @author 11's papa
 * @since 1.0.0
 */
public interface ICustomFieldService extends IService<CustomField> {

    String add(CustomField customField);

    Page<CustomField> queryPage(QueryCustomFieldRequest request);

    String updateCustomField(CustomField customField);

    List<CustomField> queryList(QueryCustomFieldRequest request);

    List<String> listIds(QueryCustomFieldRequest request);

    List<CustomField> getDefaultField(QueryCustomFieldRequest request);
}
