package com.ad.mysql.listener;

import com.ad.mysql.constant.Constant;
import com.ad.mysql.constant.OpType;
import com.ad.mysql.dto.BinlogRowData;
import com.ad.mysql.dto.MySqlRowData;
import com.ad.mysql.dto.TableTemplate;
import com.ad.sender.ISender;
import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Slf4j
@Component
public class IncrementListener implements Ilistener{
    @Resource(name = "indexSender")
    private ISender sender;
    private final AggregationListener aggregationListener;

    @Autowired
    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }
    //    实现表的注册
    @Override
    @PostConstruct
    public void register() {
        log.info("IncrementListener register db and table info");
        Constant.table2Db.forEach((k, v) ->
                aggregationListener.register(v, k, this));
    }
//
    @Override
//    这个方法主要就是把BinlogRowData转换成MySqlRowData
    public void onEvent(BinlogRowData eventData) {
        TableTemplate table = eventData.getTable();
        EventType eventType = eventData.getEventType();

        // 包装成最后需要投递的数据
        MySqlRowData rowData = new MySqlRowData();
        rowData.setTableName(table.getTableName());
        rowData.setLevel(eventData.getTable().getLevel());
        OpType opType = OpType.to(eventType);
        rowData.setOpType(opType);

        // 取出模板中该操作对应的字段列表
        List<String> fieldList = table.getOpTypeFieldSetMap().get(opType);
        if (null == fieldList) {
            log.warn("{} not support for {}", opType, table.getTableName());
            return;
        }
//        遍历event的afterMap，填充到MySqlRowData中的List<Map<String, String>> fieldValueMap中
        for (Map<String, String> afterMap : eventData.getAfter()) {
             Map<String, String> _afterMap = new HashMap<>();
            for (Map.Entry<String, String> entry : afterMap.entrySet()) {
                String colName = entry.getKey();
                String colValue = entry.getValue();
                _afterMap.put(colName, colValue);
            }
            rowData.getFieldValueMap().add(_afterMap);
        }
        sender.sender(rowData);


    }
}
