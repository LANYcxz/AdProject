package com.ad.index;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Component
public class DataTable implements ApplicationContextAware, PriorityOrdered {
//  获取bean上下文的对象？
//    private static ApplicationContext applicationContext;
//
//    public static final Map<Class, Object> dataTableMap =
//            new ConcurrentHashMap<>();
    private static ApplicationContext applicationContext;
    private static final Map<Class,Object> dataTableMap=new ConcurrentHashMap<>();
    @Override
    public void setApplicationContext(
            ApplicationContext applicationContext) throws BeansException {
        DataTable.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    //该方法是暴露出去的接口方法，比如通过DataTable.of(AdUnitIndex.class)可以得到AdUnitIndex的bean，
//就无需再使用@Autowired
//          private AdUnitIndex adUnitIndex
//这种方法来获得bean了
    @SuppressWarnings("all")
    public static <T> T of(Class<T> clazz) {
        T instance = (T) dataTableMap.get(clazz);
//        只要不是第一次加载该bean对象，就在dataTableMap找到就直接返回对的的bean对象
        if (null != instance) {
            return instance;
        }
//        以下代码只有第一次执行时才会运行，只有第一次执行才会把对应的bean加载到map中并返回
        dataTableMap.put(clazz, bean(clazz));
        return (T) dataTableMap.get(clazz);
    }

    @SuppressWarnings("all")
    private static <T> T bean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    @SuppressWarnings("all")
    private static <T> T bean(Class clazz) {
        return (T) applicationContext.getBean(clazz);
    }
}
