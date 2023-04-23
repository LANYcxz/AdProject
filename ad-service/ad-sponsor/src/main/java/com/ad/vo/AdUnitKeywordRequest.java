package com.ad.vo;

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
@AllArgsConstructor
@NoArgsConstructor
public class AdUnitKeywordRequest {
//    批量创建
    private List<UnitKeyword> unitKeywords;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitKeyword {

        private Long unitId;
        private String keyword;
    }
}
