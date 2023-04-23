package com.ad.service;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */

import com.ad.SponsorApplication;
import com.ad.exception.AdException;
import com.ad.vo.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {SponsorApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class UserServiceTest {
    @Autowired
    private IUserService userService;
    @Test
    // @Transactional
    public void testCreateUser() throws AdException {
//        测试创建用户
        CreateUserRequest createUserRequest=new CreateUserRequest("ZhanXiaowei");
        System.out.println(userService.createUser(createUserRequest));
    }
}
