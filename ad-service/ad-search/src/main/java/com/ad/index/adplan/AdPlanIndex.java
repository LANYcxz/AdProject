package com.ad.index.adplan;

import com.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Component
@Slf4j
//索引的实现类，由于是正向索引，因此泛型第一个地方的Long是索引id，第二个泛型是索引对象
public class AdPlanIndex implements IndexAware<Long,AdPlanObject> {
//map非常适合存储正向索引
    private static Map<Long,AdPlanObject> objectMap;
//使用静态代码块修饰，多线程下只加载一次ConcurrentHashMap
    static {
        objectMap=new ConcurrentHashMap<>();
    }
    @Override
    public AdPlanObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdPlanObject value) {
        log.info("before add: {}", objectMap);
        objectMap.put(key, value);
        log.info("after add: {}", objectMap);
    }

    @Override
    public void update(Long key, AdPlanObject value) {
        log.info("before update: {}", objectMap);

        AdPlanObject oldObject = objectMap.get(key);
        if (null == oldObject) {
            objectMap.put(key, value);
        } else {
            oldObject.update(value);
        }

        log.info("after update: {}", objectMap);

    }

    @Override
    public void delete(Long key, AdPlanObject value) {
        log.info("before delete: {}", objectMap);
        objectMap.remove(key);
        log.info("after delete: {}", objectMap);
    }
}
