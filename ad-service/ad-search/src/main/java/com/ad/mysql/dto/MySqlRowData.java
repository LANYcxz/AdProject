package com.ad.mysql.dto;

import com.ad.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//BinlogRowData的dto
public class MySqlRowData {

    private String tableName;

    private String level;

    private OpType opType;
//  下面的map的key是字段名，value是字段的值
    private List<Map<String, String>> fieldValueMap = new ArrayList<>();
}
