package com.ad.advice;

import com.ad.annotation.IgnoreResponseAdvice;
import com.ad.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Created By
 *
 * @author ZhanXiaowei
 */
//统一响应数据通知
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //supports方法：通过参数methodParameter调用getDeclaringClass()获取类的声明，
        // isAnnotationPresent（？）表示是否被？注解标识，如果被标识了，那么不被CommonResponse影响
        if(methodParameter.getDeclaringClass().isAnnotationPresent(
                IgnoreResponseAdvice.class
        )){
            return false;
        }
        //supports方法：通过参数methodParameter调用getMethod()获取方法的声明，
        // isAnnotationPresent（？）表示是否被？注解标识，如果被标识了，那么不被CommonResponse影响
        if(methodParameter.getMethod().isAnnotationPresent(
                IgnoreResponseAdvice.class
        )){
            return false;
        }
        return true;
    }

    @Override
    @Nullable
    @SuppressWarnings("all")
//    此方法的目的是为了拦截CommonResponse
    public Object beforeBodyWrite(@Nullable Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        CommonResponse<Object> response=new CommonResponse<>(0,"");
        if(o==null){//如果对象是null，直接返回就行
            return response;
        }else if (o instanceof CommonResponse){
//            如果就是CommonResponse类型，强转成返回即可，不需要再封装
            response= (CommonResponse<Object>) o;
        }else {
//            如果是正常对象，就封装进data中
            response.setData(o);
        }
        return response;
    }
}
