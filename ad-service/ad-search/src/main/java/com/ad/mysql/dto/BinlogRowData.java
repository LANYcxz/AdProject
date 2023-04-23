package com.ad.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Data
public class BinlogRowData {
//  声明带有位置id和字段名映射的map的table
    private TableTemplate table;
// 对应事件类型
    private EventType eventType;
//对应修改后的数据
    private List<Map<String, String>> after;
// 对应修改前的数据 一般只有update会有这个
    private List<Map<String, String>> before;
}
