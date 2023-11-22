package cn.master.gallywix.common.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.query.QueryColumnBehavior;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * @author 11's papa
 * @since 11/20/2023
 **/
@Configuration
public class MyBatisFlexConfiguration implements MyBatisFlexCustomizer {
    @Override
    public void customize(FlexGlobalConfig flexGlobalConfig) {
        // 使用内置规则自动忽略 null 和 空字符串
        QueryColumnBehavior.setIgnoreFunction(QueryColumnBehavior.IGNORE_EMPTY);
        // 使用内置规则自动忽略 null 和 空白字符串
        QueryColumnBehavior.setIgnoreFunction(QueryColumnBehavior.IGNORE_BLANK);
        // 如果传入的值是集合或数组，则使用 in 逻辑，否则使用 =（等于）
        QueryColumnBehavior.setSmartConvertInToEquals(true);
    }
}
