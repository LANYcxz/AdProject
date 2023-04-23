package com.ad.advice;

import com.ad.exception.AdException;
import com.ad.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
//统一异常处理方法
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(value = AdException.class)
    public CommonResponse<String> handlerAdException(HttpServletResponse req,
                                                     AdException ex){
//        出现异常code 返回-1。
        CommonResponse<String> response=new CommonResponse<>(-1,
                "business error!");
        response.setData(ex.getMessage());
        return response;
    }

}
