package com.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//用于推广计划的查询
public class AdPlanGetRequest {

    private Long userId;
    private List<Long>ids;

    public boolean validate(){
        return userId!=null&&!CollectionUtils.isEmpty(ids);
    }

}
