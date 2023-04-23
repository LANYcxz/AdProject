package com.ad.service;

import com.ad.Application;
import com.ad.exception.AdException;
import com.ad.vo.AdPlanGetRequest;
import com.ad.vo.AdPlanRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AdPlanServiceTest {

    @Autowired
    private IAdPlanService planService;

    @Test
    public void testGAdPlan() throws AdException {
//      测试查询
        System.out.println(planService.getAdPlanByIds(new AdPlanGetRequest(15L, Collections.singletonList(10L)))
        );

//      测试新增
        System.out.println(planService.createAdPlan(new AdPlanRequest(8L, 21L, "测试新增计划1", "2023.04.06", "2023.04.25"))
        );
        System.out.println(planService.createAdPlan(new AdPlanRequest(9L, 22L, "测试新增计划2", "2023.04.06", "2023.04.25"))
        );
        System.out.println(planService.createAdPlan(new AdPlanRequest(11L, 23L, "测试新增计划3", "2023.04.06", "2023.04.25"))
        );

//      测试更新
        System.out.println(planService.updateAdPlan(new AdPlanRequest(10L, 15L, "测试更新计划", "2022.08.07", "2023.04.07"))
        );

//      测试删除
        planService.deleteAdPlan(new AdPlanRequest(9L, 22L, "测试删除计划2", "2023.04.06", "2023.04.25")
        );
    }
}