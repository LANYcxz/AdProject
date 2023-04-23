package com.ad.client.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanGetRequest {

    private Long userId;
    private List<Long> ids;
}
