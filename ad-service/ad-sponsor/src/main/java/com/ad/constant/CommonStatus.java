package com.ad.constant;

import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
//通用常量字段,在实体类AdUnit中使用
@Getter
public enum CommonStatus {
    VALID(1,"有效状态"),
    INVALID(0,"无效状态");

    private Integer status;
    private String desc;
    CommonStatus(Integer status,String desc){
        this.status=status;
        this.desc=desc;
    }
}
