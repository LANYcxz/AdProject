package com.ad.mysql.dto;

import com.ad.mysql.constant.OpType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Data
public class ParseTemplate {
//  数据库的名称
    private String database;
//  key是表的名称，value是表的详细内容：表名、level、操作类型和字段名
    private Map<String, TableTemplate> tableTemplateMap = new HashMap<>();

    public static ParseTemplate parse(Template _template) {
//      填充ParseTemplate
        ParseTemplate template = new ParseTemplate();
        template.setDatabase(_template.getDatabase());
//      遍历获取每个table：包括表名、level、操作类型和字段名
        for (JsonTable table : _template.getTableList()) {
//          遍历Template获取表名和level
            String name = table.getTableName();
            Integer level = table.getLevel();
//      新建一个TableTemplate，为了后面把binlog的列号转为字段名
            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setTableName(name);
            tableTemplate.setLevel(level.toString());
            template.tableTemplateMap.put(name, tableTemplate);

            // 遍历操作类型对应的列，获取到TableTemplate的操作类型和字段名
            Map<OpType, List<String>> opTypeFieldSetMap =
                    tableTemplate.getOpTypeFieldSetMap();
//          根据Template的tableList的对象table，调用其的getInsert,getUpdate,getDelete方法
//          获取到对应操作类型的类column，再调用column的getColumn方法填充TableTemplate中声明的
//          opTypeFieldSetMap的value，我们知道这个value就是列名字，key就是操作类型
//          这样就可以填充好opTypeFieldSetMap，我们监听binlog的时候就可以通过tableTemplate获取列名
            for (JsonTable.Column column : table.getInsert()) {
//          下面的方法是通过定义一个泛型方法来使用的
                getAndCreateIfNeed(
                        OpType.ADD,//key
                        opTypeFieldSetMap,//map
                        ArrayList::new//value不存在则填充一个新的为空的list
                ).add(column.getColumn());//存在就调用map的add方法，根据key把对应的value填充到map
            }
            for (JsonTable.Column column : table.getUpdate()) {
                getAndCreateIfNeed(
                        OpType.UPDATE,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumn());
            }
            for (JsonTable.Column column : table.getDelete()) {
                getAndCreateIfNeed(
                        OpType.DELETE,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumn());
            }
        }

        return template;
    }
//泛型方法我们的map是opTypeFieldSetMap<String,List<String>>；
    private static <T, R> R getAndCreateIfNeed(T key, Map<T, R> map,
                                               Supplier<R> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }
}
