package com.ad.utils;

import com.ad.exception.AdException;
import org.apache.http.client.utils.DateUtils;
import org.springframework.util.DigestUtils;

import java.util.Date;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
public class CommonUtils {


    public static String md5(String value) {
        return DigestUtils.md5DigestAsHex(value.getBytes()).toUpperCase();
    }

    //用在vo的AdPlanRequest中
    private static String[] parsePattrns = {
            "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd"
    };
    public static Date parseStringDate(String dateString) throws AdException {
        try {
            return DateUtils.parseDate(dateString, parsePattrns);
        } catch (Exception e) {
            throw new AdException(e.getMessage());
        }
    }
}
