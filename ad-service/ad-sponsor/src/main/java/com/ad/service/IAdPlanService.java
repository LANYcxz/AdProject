package com.ad.service;

import com.ad.entity.AdPlan;
import com.ad.exception.AdException;
import com.ad.vo.AdPlanGetRequest;
import com.ad.vo.AdPlanRequest;
import com.ad.vo.AdPlanResponse;

import java.util.List;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
public interface IAdPlanService {
    /**
     * 创建推广计划
     */
    AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException;

    /**
     * 批量获取推广计划
     */
    List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException;

    /**
     * 更新推广计划
     */
    AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException;

    /**
     * 删除推广计划
     */
    void deleteAdPlan(AdPlanRequest request) throws  AdException;
}
