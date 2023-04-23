package com.ad.service.impl;

import com.ad.constant.Constants;
import com.ad.dao.AdUserRepository;
import com.ad.entity.AdUser;
import com.ad.exception.AdException;
import com.ad.service.IUserService;
import com.ad.utils.CommonUtils;
import com.ad.vo.CreateUserRequest;
import com.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
//  自动装配repository对象，系统自动修改为构造函数方法
    private final AdUserRepository userRepository;
    @Autowired
    public UserServiceImpl(AdUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    @Transactional
//    创建用户的service
    public CreateUserResponse createUser(CreateUserRequest request) throws AdException {
//        用vo的写的校验方法，要根据姓名查询，首先需要姓名不为空！
//        因此定义可能存在的错误信息，先定义错误常量，在常量包的Constants下
//        异常交给ad-common下的统一异常管理
        if(!request.validate()){
            throw new AdException(Constants.ErroMsg.REQUEST_PARAM_ERROR);
        }
//      创建新用户，不能存在重复用户，那么要先去数据库查询
        AdUser oldUser=userRepository.findByUsername(request.getUsername());
        if(oldUser!=null){
//       用户姓名已存在，抛出异常，去Constants定义
            throw new AdException(Constants.ErroMsg.SAME_NAME_ERROR);

        }
//        如果用户可以新建，那么存入数据库，注意我们AdUser类的构造方法
//        只需要传入两个参数：username和token，token我们自己在utils
//        包下创建一个加密MD5的字符串即可。
        AdUser newUser=userRepository.save(new AdUser(
                request.getUsername(),
                CommonUtils.md5(request.getUsername())));
//        最后通过vo包下的Response返回
        return new CreateUserResponse(newUser.getId(),newUser.getUsername(),newUser.getToken(),
                newUser.getCreateTime(),newUser.getUpdateTime());
    }
}
