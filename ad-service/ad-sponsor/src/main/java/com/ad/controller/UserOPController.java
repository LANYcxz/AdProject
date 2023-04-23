package com.ad.controller;

import com.ad.exception.AdException;
import com.ad.service.IUserService;
import com.ad.vo.CreateUserRequest;
import com.ad.vo.CreateUserResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Slf4j
@RestController
//yml文件中，所有路径都是/ad-sponsor开头
public class UserOPController {
    private final IUserService userService;

    public UserOPController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create/user")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request)throws AdException{
        log.info("ad-sponsor:createUser -> {}", JSON.toJSONString(request));
        return userService.createUser(request);
    }
}
