package com.ad.mysql.dto;

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
public class JsonTable {
    private String tableName;
    private Integer level;

    private List<Column> insert;
    private List<Column> update;
    private List<Column> delete;
//    使用静态内部类这样子通常是为了对应的json格式转换！！！看template.json就懂了
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
//    嵌套的字段要使用静态内部类
//    insert类型中是一系列的column字段，每一个column都是一个字符串
    public static class Column{
        private String column;
    }


}
