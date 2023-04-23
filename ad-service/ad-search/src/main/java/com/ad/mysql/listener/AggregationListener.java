package com.ad.mysql.listener;

import com.ad.mysql.TemplateHolder;
import com.ad.mysql.dto.BinlogRowData;
import com.ad.mysql.dto.TableTemplate;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.ad.mysql.TemplateHolder;
import com.ad.mysql.dto.BinlogRowData;
import com.ad.mysql.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Slf4j
@Component
//解析和监听binlog
public class AggregationListener implements BinaryLogClient.EventListener {
//  binlog一定要输出数据库的名字和表名
    private String dbName;
    private String tableName;
// binlog解析出的对象，我们需要一些处理方法用来注册监听器、提供事件等等
// 由于每个数据库的每张表都可以用不同的监听方法处理，所以定义成map，key是表名，value是监听器
    private Map<String, Ilistener> listenerMap = new HashMap<>();
//载入模板信息
    private final TemplateHolder templateHolder;
    @Autowired
    public AggregationListener(TemplateHolder templateHolder) {
        this.templateHolder = templateHolder;
    }

//提供一个方法，用数据库名+表名唯一标识一张表
    private String genKey(String dbName, String tableName) {
        return dbName + ":" + tableName;
    }
//实现一个注册监听器的方法，让外界去调用
    public void register(String _dbName, String _tableName,
                         Ilistener ilistener) {
        log.info("register : {}-{}", _dbName, _tableName);
        this.listenerMap.put(genKey(_dbName, _tableName), ilistener);
    }
//  将binlog数据传送给listener，然后才能进一步实现检索系统增量数据的更新
    @Override
    public void onEvent(Event event) {
//  先获取eventType，根据eventType的类型进行下一步操作
        EventType type = event.getHeader().getEventType();
        log.debug("event type: {}", type);
//  如果是EventType.TABLE_MAP类型含义是：记录下一个操作所对应的表信息，存储了数据库名和表名
//  我们记录完不进行其他操作直接返回
        if (type == EventType.TABLE_MAP) {
            TableMapEventData data = event.getData();
//          把binlog监听的数据库和表记录下来
            this.tableName = data.getTable();
            this.dbName = data.getDatabase();
            return;
        }
//      除了需要的三种操作类型需要监听，其余都不监听
        if (type != EventType.EXT_UPDATE_ROWS
                && type != EventType.EXT_WRITE_ROWS
                && type != EventType.EXT_DELETE_ROWS) {
            return;
        }
        // 表名和库名是否已经完成填充，如果没有填充说明是一个错误的binlog事件
        if (StringUtils.isEmpty(dbName) || StringUtils.isEmpty(tableName)) {
            log.error("no meta data event");
            return;
        }

        // 找出对应表有兴趣的监听器，即模板json中才有的表
        String key = genKey(this.dbName, this.tableName);
        Ilistener listener = this.listenerMap.get(key);
        if (null == listener) {
            log.debug("skip {}", key);
            return;
        }
//      不为空，会触发事件，监听binlog
        log.info("trigger event: {}", type.name());

        try {
//      如果是我们想操作的表，那就直接调用buildRowData方法，把data传入转换成java对象
            BinlogRowData rowData = buildRowData(event.getData());
            if (rowData == null) {
                return;
            }
            rowData.setEventType(type);
            listener.onEvent(rowData);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        } finally {
            this.dbName = "";
            this.tableName = "";
        }
    }
//  获取我们对应操作的对应数据的一行记录
    private List<Serializable[]> getAfterValues(EventData eventData) {
        if (eventData instanceof WriteRowsEventData) {
            return ((WriteRowsEventData) eventData).getRows();
        }
        if (eventData instanceof UpdateRowsEventData) {
            return ((UpdateRowsEventData) eventData).getRows().stream()
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }
        if (eventData instanceof DeleteRowsEventData) {
            return ((DeleteRowsEventData) eventData).getRows();
        }

        return Collections.emptyList();
    }
//该方法用于实现将eventdata转换为BinlogRowData
    private BinlogRowData buildRowData(EventData eventData) {

        TableTemplate table = templateHolder.getTable(tableName);
        if (null == table) {
            log.warn("table {} not found", tableName);
            return null;
        }
        List<Map<String, String>> afterMapList = new ArrayList<>();
//      调用getAfterValues方法，把事件传入以后得到rows=[ before=[...],after=[...] ]，不过通常before我们不在乎,一般就得到after
        for (Serializable[] after : getAfterValues(eventData)) {//这里的after是个list
//          新建一个afterMap，用来填充BinlogRowData的after
            Map<String, String> afterMap = new HashMap<>();
            int colLen = after.length;
            for (int ix = 0; ix < colLen; ++ix) {//根据长度，慢慢遍历，与从json文件读入的列进行比较
                // 取出当前位置对应的列名
                String colName = table.getPosMap().get(ix);

                // 如果没有则说明不关心这个列
                if (null == colName) {
                    log.debug("ignore position: {}", ix);
                    continue;
                }
//  如果存在这样的row，也就是binlog监听到了json文件中声明的表和字段，那么会把字段名和对应的值存入到afterMap中
                String colValue = after[ix].toString();
                afterMap.put(colName, colValue);
            }

            afterMapList.add(afterMap);
        }
//  最后包装成BinlogRowData返回
        BinlogRowData rowData = new BinlogRowData();
        rowData.setAfter(afterMapList);
        rowData.setTable(table);

        return rowData;
    }
}
