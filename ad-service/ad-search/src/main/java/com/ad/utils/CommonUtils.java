package com.ad.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Slf4j
public class CommonUtils {
//  判断传入的map集合中是否存在给的key，若不存在则new一个东西到value中，对应到这里就是factory.get
    public static <K, V> V getorCreate(K key, Map<K, V> map,
                                       Supplier<V> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }

    public static String stringConcat(String... args) {

        StringBuilder result = new StringBuilder();
        for (String arg : args) {
            result.append(arg);
            result.append("-");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    // Tue Jan 01 08:00:00 CST 2019
//    该方法用在binlog把日期解析date类型的转换
    public static Date parseStringDate(String dateString) {

        try {

            DateFormat dateFormat = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy",
                    Locale.US
            );
            return DateUtils.addHours(
                    dateFormat.parse(dateString),
                    -8
            );

        } catch (ParseException ex) {
            log.error("parseStringDate error: {}", dateString);
            return null;
        }
    }
}
