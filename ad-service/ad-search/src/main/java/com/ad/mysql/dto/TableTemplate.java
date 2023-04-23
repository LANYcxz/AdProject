package com.ad.mysql.dto;

import com.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//和JsonTable一样，只不过表现形式不同，这里把操作类型insert、update、delete集中成一个枚举类OpType
public class TableTemplate {
    private String tableName;
    private String level;
//    OpType对应JsonTable表的insert,update.delete
//    List对应了column，即字段名
    private Map<OpType, List<String>> opTypeFieldSetMap = new HashMap<>();
    /**
     * 字段索引 -> 字段名
     * */
    private Map<Integer, String> posMap = new HashMap<>();
}
