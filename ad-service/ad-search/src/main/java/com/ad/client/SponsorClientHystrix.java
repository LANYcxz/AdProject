package com.ad.client;

import com.ad.client.vo.AdPlan;
import com.ad.client.vo.AdPlanGetRequest;
import com.ad.vo.CommonResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
//feign断路器降级方法，要实现我们定义的SponsorClient接口
@Component
public class SponsorClientHystrix implements SponsorClient {

    @Override
    public CommonResponse<List<AdPlan>> getAdPlans(
            AdPlanGetRequest request) {
        return new CommonResponse<>(-1,
                "eureka-client-ad-sponsor error");
    }
}
