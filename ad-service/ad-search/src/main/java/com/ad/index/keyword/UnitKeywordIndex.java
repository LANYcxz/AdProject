package com.ad.index.keyword;

import com.ad.index.IndexAware;
import com.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
//使用倒排索引，key是关键词，value是对应的Unit id的集合
@Slf4j
@Component
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {
//  这个map用来存储倒排索引，一个关键词可以对应多个推广单元的id。
    private static Map<String,Set<Long>> keywordUnitMap;
//  这个map用来存储正排索引，是数据库中对应的行记录，一个id对应多个推广单元的keyword
    private static Map<Long,Set<String>> unitKeywordMap;
    static {
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }
    @Override
    public Set<Long> get(String key) {
        if(StringUtils.isEmpty(key)){
            return Collections.emptySet();
        }
        Set<Long> result = keywordUnitMap.get(key);
        if(result==null){
            return Collections.emptySet();
        }
        return result;
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("UnitKeywordIndex, before add: {}", unitKeywordMap);

        Set<Long> unitIdSet = CommonUtils.getorCreate(
                key, keywordUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIdSet.addAll(value);
        for (Long unitId : value) {
            Set<String> keywordSet = CommonUtils.getorCreate(
                    unitId, unitKeywordMap,
                    ConcurrentSkipListSet::new
            );
            keywordSet.add(key);
        }

        log.info("UnitKeywordIndex, after add: {}", unitKeywordMap);
    }
//更新成本太高，不支持更新
    @Override
    public void update(String key, Set<Long> value) {
        log.error("keyword index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitKeywordIndex, before delete: {}", unitKeywordMap);

        Set<Long> unitIds = CommonUtils.getorCreate(
                key, keywordUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.removeAll(value);

        for (Long unitId : value) {

            Set<String> keywordSet = CommonUtils.getorCreate(
                    unitId, unitKeywordMap,
                    ConcurrentSkipListSet::new
            );
            keywordSet.remove(key);
        }
        log.info("UnitKeywordIndex, after delete: {}", unitKeywordMap);
    }
    public boolean match(Long unitId, List<String> keywords) {

        if (unitKeywordMap.containsKey(unitId)
                && CollectionUtils.isNotEmpty(unitKeywordMap.get(unitId))) {

            Set<String> unitKeywords = unitKeywordMap.get(unitId);

            return CollectionUtils.isSubCollection(keywords, unitKeywords);
        }

        return false;
    }
}
