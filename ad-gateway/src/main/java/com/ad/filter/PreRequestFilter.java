package com.ad.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * Created By
 *
 * @author ZhanXiaowei
 */
@Slf4j
@Component
//前置过滤器
public class PreRequestFilter extends ZuulFilter {
    @Override
    public String filterType() {
//      声明过滤器的类型（前置还是路由还是后置）
        return FilterConstants.PRE_TYPE;
    }
    @Override
    public int filterOrder() {
        return 0;
    }
    @Override
    public boolean shouldFilter() {
//  永远执行该过滤器，如果设置为false说明只有在某些条件下才执行
        return true;
    }
    @Override
    public Object run() throws ZuulException {
//      具体执行方法
        log.info("正在经过一个前置过滤器");
//      使用请求上下文来保存信息给后面的Filter，类似于ThreadLocal
        RequestContext currentContext = RequestContext.getCurrentContext();
        currentContext.set("startTime", System.currentTimeMillis());
        return null;
    }
}
