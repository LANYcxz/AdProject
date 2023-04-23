package com.ad.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created By
 *
 * @author ZhanXiaowei
 */
@Slf4j
@Component
//后置过滤器
public class AccessLogFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
//        先获取上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
//      从pre过滤器得到startTime
        Long startTime= (Long) currentContext.get("startTime");
// 从request获取URI
        HttpServletRequest request = currentContext.getRequest();
        String uri = request.getRequestURI();
//相减得到URI经过网关的时间
        long duration= System.currentTimeMillis()-startTime;
        log.info("uri:"+uri+",duration:"+duration/100+"ms");
        return null;
    }
}
