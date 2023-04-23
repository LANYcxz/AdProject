package com.ad.handler;

import com.ad.dump.table.*;
import com.ad.index.DataTable;
import com.ad.index.IndexAware;
import com.ad.index.adplan.AdPlanIndex;
import com.ad.index.adplan.AdPlanObject;
import com.ad.index.adunit.AdUnitIndex;
import com.ad.index.adunit.AdUnitObject;
import com.ad.index.creative.CreativeIndex;
import com.ad.index.creative.CreativeObject;
import com.ad.index.creativeunit.CreativeUnitIndex;
import com.ad.index.creativeunit.CreativeUnitObject;
import com.ad.index.district.UnitDistrictIndex;
import com.ad.index.interest.UnitItIndex;
import com.ad.index.keyword.UnitKeywordIndex;
import com.ad.mysql.constant.OpType;
import com.ad.utils.CommonUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Slf4j
//既能处理全量索引，也能处理增量索引
public class AdLevelDataHandler {
//二级索引，其他索引会依赖于二级索引，但二级索引不会依赖于其他索引。
    public static void handleLevel2(AdPlanTable planTable, OpType type) {

        AdPlanObject planObject = new AdPlanObject(
                planTable.getId(),
                planTable.getUserId(),
                planTable.getPlanStatus(),
                planTable.getStartDate(),
                planTable.getEndDate()
        );
        handleBinlogEvent(
                DataTable.of(AdPlanIndex.class),
                planObject.getPlanId(),
                planObject,
                type
        );
    }
    public static void handleLevel2(AdCreativeTable creativeTable,
                                    OpType type) {

        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getAdUrl()
        );
        handleBinlogEvent(
                DataTable.of(CreativeIndex.class),
                creativeObject.getAdId(),
                creativeObject,
                type
        );
    }
//第三级索引，依赖于二级索引
public static void handleLevel3(AdUnitTable unitTable, OpType type) {

    AdPlanObject adPlanObject = DataTable.of(
            AdPlanIndex.class
    ).get(unitTable.getPlanId());
    if (null == adPlanObject) {
        log.error("handleLevel3 found AdPlanObject error: {}",
                unitTable.getPlanId());
        return;
    }

    AdUnitObject unitObject = new AdUnitObject(
            unitTable.getUnitId(),
            unitTable.getUnitStatus(),
            unitTable.getPositionType(),
            unitTable.getPlanId(),
            adPlanObject
    );

    handleBinlogEvent(
            DataTable.of(AdUnitIndex.class),
            unitTable.getUnitId(),
            unitObject,
            type
    );
}

    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable,
                                    OpType type) {

        if (type == OpType.UPDATE) {
            log.error("CreativeUnitIndex not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(
                CreativeIndex.class
        ).get(creativeUnitTable.getAdId());

        if (null == unitObject || null == creativeObject) {
            log.error("AdCreativeUnitTable index error: {}",
                    JSON.toJSONString(creativeUnitTable));
            return;
        }

        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                creativeUnitTable.getAdId(),
                creativeUnitTable.getUnitId()
        );
        handleBinlogEvent(
                DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringConcat(
                        creativeUnitObject.getAdId().toString(),
                        creativeUnitObject.getUnitId().toString()
                ),
                creativeUnitObject,
                type
        );
    }

//    第四级索引
public static void handleLevel4(AdUnitDistrictTable unitDistrictTable,
                                OpType type) {

    if (type == OpType.UPDATE) {
        log.error("district index can not support update");
        return;
    }

    AdUnitObject unitObject = DataTable.of(
            AdUnitIndex.class
    ).get(unitDistrictTable.getUnitId());
    if (unitObject == null) {
        log.error("AdUnitDistrictTable index error: {}",
                unitDistrictTable.getUnitId());
        return;
    }

    String key = CommonUtils.stringConcat(
            unitDistrictTable.getProvince(),
            unitDistrictTable.getCity()
    );
    Set<Long> value = new HashSet<>(
            Collections.singleton(unitDistrictTable.getUnitId())
    );
    handleBinlogEvent(
            DataTable.of(UnitDistrictIndex.class),
            key, value,
            type
    );
}

    public static void handleLevel4(AdUnitItTable unitItTable, OpType type) {

        if (type == OpType.UPDATE) {
            log.error("it index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(unitItTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitItTable index error: {}",
                    unitItTable.getUnitId());
            return;
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(unitItTable.getUnitId())
        );
        handleBinlogEvent(
                DataTable.of(UnitItIndex.class),
                unitItTable.getItTag(),
                value,
                type
        );
    }

    public static void handleLevel4(AdUnitKeywordTable keywordTable,
                                    OpType type) {

        if (type == OpType.UPDATE) {
            log.error("keyword index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(
                AdUnitIndex.class
        ).get(keywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error: {}",
                    keywordTable.getUnitId());
            return;
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(keywordTable.getUnitId())
        );
        handleBinlogEvent(
                DataTable.of(UnitKeywordIndex.class),
                keywordTable.getKeyword(),
                value,
                type
        );
    }

    private static<K,V> void handleBinlogEvent(
            IndexAware<K,V> index,
            K key,
            V value,
            OpType type){
        switch (type){
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
                break;
            case DELETE:
                index.delete(key, value);
                break;
            default:
                break;
        }
    }

}
