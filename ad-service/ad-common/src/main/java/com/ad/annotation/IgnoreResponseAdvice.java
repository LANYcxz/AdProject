package com.ad.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created By
 *
 * @author ZhanXiaowei
 */
//被该注释标记的方法或者类就不被CommonResponse处理
    @Target({ElementType.TYPE,ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseAdvice {
}
