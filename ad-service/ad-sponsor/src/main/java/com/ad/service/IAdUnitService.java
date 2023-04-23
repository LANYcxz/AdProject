package com.ad.service;

import com.ad.exception.AdException;
import com.ad.vo.*;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
public interface IAdUnitService {
    AdUnitResponse createUnit(AdUnitRequest request)throws AdException;
    AdUnitKeywordResponse createUnitKeyword(AdUnitKeywordRequest request) throws AdException;
    AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException;
    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request ) throws AdException;
    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request)
            throws AdException;
}
