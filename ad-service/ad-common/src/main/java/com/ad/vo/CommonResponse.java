package com.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created By
 *
 * @author ZhanXiaowei
 */
//统一响应的类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {
    public Integer code;
    public String message;
    public T data;
    public CommonResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
