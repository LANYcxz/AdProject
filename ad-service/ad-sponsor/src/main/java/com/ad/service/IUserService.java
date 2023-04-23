package com.ad.service;

import com.ad.exception.AdException;
import com.ad.vo.CreateUserRequest;
import com.ad.vo.CreateUserResponse;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
public interface IUserService {

    /**
     * <h2>创建用户</h2>
     **/
    CreateUserResponse createUser(CreateUserRequest request)
            throws AdException;
}
