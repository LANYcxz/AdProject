package com.ad.service.impl;

import com.ad.constant.Constants;
import com.ad.dao.AdPlanRepository;
import com.ad.dao.AdUnitRepository;
import com.ad.dao.CreativeRepository;
import com.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.ad.dao.unit_condition.AdUnitItRepository;
import com.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.ad.dao.unit_condition.CreativeUnitRepository;
import com.ad.entity.AdPlan;
import com.ad.entity.AdUnit;
import com.ad.entity.Creative;
import com.ad.entity.unit_condition.AdUnitDistrict;
import com.ad.entity.unit_condition.AdUnitIt;
import com.ad.entity.unit_condition.AdUnitKeyword;
import com.ad.entity.unit_condition.CreativeUnit;
import com.ad.exception.AdException;
import com.ad.service.IAdUnitService;
import com.ad.vo.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Service
public class AdUnitServiceImpl implements IAdUnitService {
    private final AdPlanRepository planRepository;
    private final AdUnitRepository unitRepository;

    private final AdUnitKeywordRepository unitKeywordRepository;
    private final AdUnitItRepository unitItRepository;
    private final AdUnitDistrictRepository unitDistrictRepository;
    private final CreativeRepository creativeRepository;
    private final CreativeUnitRepository creativeUnitRepository;
    public AdUnitServiceImpl(AdPlanRepository planRepository, AdUnitRepository unitRepository, AdUnitKeywordRepository unitKeywordRepository, AdUnitItRepository unitItRepository, AdUnitDistrictRepository unitDistrictRepository, CreativeRepository creativeRepository, CreativeUnitRepository creativeUnitRepository) {
        this.planRepository = planRepository;
        this.unitRepository = unitRepository;
        this.unitKeywordRepository = unitKeywordRepository;
        this.unitItRepository = unitItRepository;
        this.unitDistrictRepository = unitDistrictRepository;
        this.creativeRepository = creativeRepository;
        this.creativeUnitRepository = creativeUnitRepository;
    }


    @Override
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {
        if(!request.createValidate()){
            throw new AdException(Constants.ErroMsg.REQUEST_PARAM_ERROR);
        }
        Optional<AdPlan> adPlan=planRepository.findById(request.getPlanId());
        if(!adPlan.isPresent()){
            throw new AdException(Constants.ErroMsg.CAN_NOT_FIND_RECORD);

        }
        AdUnit oldAdUnit = unitRepository.findByPlanIdAndUnitName(
                request.getPlanId(), request.getUnitName()
        );
//        如果已经存在抛出异常。
        if (oldAdUnit != null) {
            throw new AdException(Constants.ErroMsg.SAME_NAME_UNIT_ERROR);
        }
        AdUnit newAdUnit=unitRepository.save(
                new AdUnit(request.getPlanId(), request.getUnitName(), request.getPositionType(), request.getBudget())
        );
        return new AdUnitResponse(newAdUnit.getId(),newAdUnit.getUnitName());
    }
// 以下三个接口方法我们定义出通用的方法校验，因为都是判断推广单元的ids；
//  以此来判断推广单元的关键词等限制
    @Override
    public AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException {
// 使用stream流来接受前端传来的批量id，判断推广单元和推广关键词有没有关系
        List<Long> unitIds=request.getUnitKeywords().stream()
                .map(AdUnitKeywordRequest.UnitKeyword::getUnitId)
                .collect(Collectors.toList());
        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErroMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> ids = Collections.emptyList();
        List<AdUnitKeyword> unitKeywords = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitKeywords())) {

            request.getUnitKeywords().forEach(i -> unitKeywords.add(
                    new AdUnitKeyword(i.getUnitId(), i.getKeyword())
            ));
            ids = unitKeywordRepository.saveAll(unitKeywords).stream()
                    .map(AdUnitKeyword::getId)
                    .collect(Collectors.toList());
        }

        return new AdUnitKeywordResponse(ids);
    }

    @Override
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {

        List<Long> unitIds = request.getUnitIts().stream()
                .map(AdUnitItRequest.UnitIt::getUnitId)
                .collect(Collectors.toList());
        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErroMsg.REQUEST_PARAM_ERROR);
        }
        List<AdUnitIt> unitIts=new ArrayList<>();
        request.getUnitIts().forEach(i->{unitIts.add(new AdUnitIt(i.getUnitId(),i.getItTag()));});
        List<Long> ids=unitItRepository.saveAll(unitIts).stream()
                .map(AdUnitIt::getId)
                .collect(Collectors.toList());
        return new AdUnitItResponse(ids);
    }

    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {
        List<Long> unitIds = request.getUnitDistricts().stream()
                .map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .collect(Collectors.toList());
        if(!isRelatedUnitExist(unitIds)){
            throw new AdException(Constants.ErroMsg.REQUEST_PARAM_ERROR);
        }
        List<AdUnitDistrict> unitDistricts=new ArrayList<>();
        request.getUnitDistricts().forEach(d->unitDistricts.add(new AdUnitDistrict(d.getUnitId(),d.getProvince(),d.getCity())));
        List<Long> ids = unitDistrictRepository.saveAll(unitDistricts)
                .stream().map(AdUnitDistrict::getId)
                .collect(Collectors.toList());
        return new AdUnitDistrictResponse(ids);
    }

    @Override
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {
        List<Long> unitIds = request.getUnitItems().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getUnitId)
                .collect(Collectors.toList());
        List<Long> creativeIds = request.getUnitItems().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getCreativeId)
                .collect(Collectors.toList());

        if (!(isRelatedUnitExist(unitIds) && isRelatedUnitExist(creativeIds))) {
            throw new AdException(Constants.ErroMsg.REQUEST_PARAM_ERROR);
        }

        List<CreativeUnit> creativeUnits = new ArrayList<>();
        request.getUnitItems().forEach(i -> creativeUnits.add(
                new CreativeUnit(i.getCreativeId(), i.getUnitId())
        ));

        List<Long> ids = creativeUnitRepository.saveAll(creativeUnits)
                .stream()
                .map(CreativeUnit::getId)
                .collect(Collectors.toList());

        return new CreativeUnitResponse(ids);
    }

    private boolean isRelatedUnitExist(List<Long> unitIds){
        if(CollectionUtils.isEmpty(unitIds)){
            return false;
        }
        return unitRepository.findAllById(unitIds).size()==
                new HashSet<>(unitIds).size();
    }

    private boolean isRelatedCreativeExist(List<Long> creativeIds) {

        if (CollectionUtils.isEmpty(creativeIds)) {
            return false;
        }

        return creativeRepository.findAllById(creativeIds).size() ==
                new HashSet<>(creativeIds).size();
    }
}
