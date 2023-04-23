package com.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//用在创建、更新、删除推广计划上面
public class AdPlanRequest {
    private Long id;
    private Long userId;
    private String planName;
// 注意这里我们用了字符串来表示日期，到时候需要进行序列化处理转换成
// mysql的date类型，我们在utils包的CommonUtils写转换方法
    private String startDate;
    private String endDate;
    public boolean createValidate(){
        return userId!=null
                &&! StringUtils.isEmpty(planName)
                &&!StringUtils.isEmpty(startDate)
                &&! StringUtils.isEmpty(endDate);
    }
    public boolean updateValidate(){
        return id != null && userId != null;
    }
    public boolean deleteValidate(){
        return id != null && userId != null;
    }

}
