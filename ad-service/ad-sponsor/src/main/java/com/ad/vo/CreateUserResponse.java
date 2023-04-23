package com.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponse {

    private Long userId;
    private String username;
    private String token;
    private Date createTime;
    private Date updateTime;
}
