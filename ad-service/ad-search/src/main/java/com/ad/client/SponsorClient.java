package com.ad.client;

import com.ad.client.vo.AdPlan;
import com.ad.client.vo.AdPlanGetRequest;
import com.ad.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 * 当前注册的微服务所在：eureka-client-ad-search
 */
@FeignClient(name="eureka-client-ad-sponsor",fallback = SponsorClientHystrix.class)
public interface SponsorClient {

    @RequestMapping(value = "/ad-sponsor/get/adPlan",method = RequestMethod.POST)
    public CommonResponse<List<AdPlan>> getAdPlans(
      @RequestBody AdPlanGetRequest request
    );

}
