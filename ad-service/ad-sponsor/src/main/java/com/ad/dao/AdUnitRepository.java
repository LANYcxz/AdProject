package com.ad.dao;

import com.ad.entity.AdUnit;
import com.ad.entity.AdUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
public interface AdUnitRepository extends JpaRepository<AdUnit, Long> {

    AdUnit findByPlanIdAndUnitName(Long planId, String unitName);

    List<AdUnit> findAllByUnitStatus(Integer unitStatus);
}
