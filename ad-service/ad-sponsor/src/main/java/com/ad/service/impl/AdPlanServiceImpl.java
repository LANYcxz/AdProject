package com.ad.service.impl;

import com.ad.constant.CommonStatus;
import com.ad.constant.Constants;
import com.ad.dao.AdPlanRepository;
import com.ad.dao.AdUserRepository;
import com.ad.entity.AdPlan;
import com.ad.entity.AdUser;
import com.ad.exception.AdException;
import com.ad.service.IAdPlanService;
import com.ad.utils.CommonUtils;
import com.ad.vo.AdPlanGetRequest;
import com.ad.vo.AdPlanRequest;
import com.ad.vo.AdPlanResponse;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Service
@Slf4j
public class AdPlanServiceImpl implements IAdPlanService {
    private final AdUserRepository userRepository;

    private final AdPlanRepository planRepository;
    @Autowired
    public AdPlanServiceImpl(AdUserRepository userRepository, AdPlanRepository planRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @Override
    @Transactional
    public AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException {
//      如果请求参数不合法，抛出异常
        if(!request.createValidate()){
            throw new AdException(Constants.ErroMsg.REQUEST_PARAM_ERROR);
        }
//       确保关联的User存在,用到jdk8新特性Optional
        Optional<AdUser> adUser=userRepository.findById(request.getUserId());
        if (!adUser.isPresent()) {
//        用户不存在就抛出异常
            throw new AdException(Constants.ErroMsg.CAN_NOT_FIND_RECORD);
        }
//       判断旧的plan是否存在
        AdPlan oldPlan= planRepository.findByUserIdAndPlanName(request.getUserId(),
                request.getPlanName());
        if(oldPlan!=null){
            throw new AdException(Constants.ErroMsg.SAME_NAME_PLAN_ERROR);
        }
//        注意这里传入计划的日期时要使用工具包下CommonUtils把String类型转换为Date
        AdPlan newAdPlan = planRepository.save(new AdPlan(request.getUserId(), request.getPlanName(),
                CommonUtils.parseStringDate(request.getStartDate()),
                CommonUtils.parseStringDate(request.getEndDate())));

        return new AdPlanResponse(newAdPlan.getId(),newAdPlan.getPlanName());
    }

    @Override
    public List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException {
       if(!request.validate()){
           throw new AdException(Constants.ErroMsg.REQUEST_PARAM_ERROR);
       }

        return planRepository.findAllByIdInAndUserId(request.getIds(),
                request.getUserId());
    }

    @Override
    @Transactional
    public AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException {
        if(!request.updateValidate()){
            throw new AdException(Constants.ErroMsg.REQUEST_PARAM_ERROR);
        }
        AdPlan plan = planRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (plan==null){
            throw new AdException(Constants.ErroMsg.CAN_NOT_FIND_RECORD);
        }
        if (request.getPlanName() != null) {
            plan.setPlanName(request.getPlanName());
        }
        if (request.getStartDate() != null) {
            plan.setStartDate(
                    CommonUtils.parseStringDate(request.getStartDate())
            );
        }
        if (request.getEndDate() != null) {
            plan.setEndDate(
                    CommonUtils.parseStringDate(request.getEndDate())
            );
        }

        plan.setUpdateTime(new Date());
        plan = planRepository.save(plan);

        return new AdPlanResponse(plan.getId(), plan.getPlanName());
    }

    @Override
    @Transactional
    public void deleteAdPlan(AdPlanRequest request) throws AdException {
        if(!request.deleteValidate()){
            throw new AdException(Constants.ErroMsg.REQUEST_PARAM_ERROR);
        }

        AdPlan plan=planRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (plan==null) {
            throw new AdException(Constants.ErroMsg.CAN_NOT_FIND_RECORD);
        }
//        删除前需要把对应的status设置为无效状态,逻辑删除
        plan.setPlanStatus(CommonStatus.INVALID.getStatus());
        plan.setUpdateTime(new Date());
        planRepository.save(plan);
    }
}
