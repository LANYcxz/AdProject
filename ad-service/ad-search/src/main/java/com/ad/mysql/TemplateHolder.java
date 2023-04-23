package com.ad.mysql;

import com.ad.mysql.constant.OpType;
import com.ad.mysql.dto.ParseTemplate;
import com.ad.mysql.dto.TableTemplate;
import com.ad.mysql.dto.Template;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
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
public class TemplateHolder {
    private ParseTemplate template;
    private final JdbcTemplate jdbcTemplate;
//  定义查询的sql语句
    private String SQL_SCHEMA = "select table_schema, table_name, " +
            "column_name, ordinal_position from information_schema.columns " +
            "where table_schema = ? and table_name = ?";

    @Autowired
    public TemplateHolder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
//  使用该注解，容器启动就会执行，就会加载json文件
    @PostConstruct
    private void init() {
        loadJson("template.json");
    }
//  提供给对外的接口，返回表的相关信息，只需要输入要查询的表名，就可以得到表的等级、操作类型和字段名等等
    public TableTemplate getTable(String tableName) {
        return template.getTableTemplateMap().get(tableName);
    }
//  加载模板json文件方法，最重要的方法,path就是json文件名
    private void loadJson(String path) {
//      通过ClassLoader可以获得resource下定义的文件
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
//      通过输入流读取json文件
        InputStream inStream = cl.getResourceAsStream(path);
        try {//模板json对应的java实体类是Template类，使用JSON工具反序列化json为模板类
            Template template = JSON.parseObject(
                    inStream,
                    Charset.defaultCharset(),
                    Template.class
            );
            this.template = ParseTemplate.parse(template);
//          加载完成以后，就需要binlog实现索引到字段名的映射，调用loadMeta方法
            loadMeta();
        } catch (IOException ex) {
            log.error(ex.getMessage());
//          如果加载json失败，直接返回运行时异常
            throw new RuntimeException("fail to parse json file");
        }
    }
//该方法是填充binlog的位置id对应的到的列名的转换，详细的类在TableTemplate类中
    private void loadMeta() {
//      遍历ParseTemplate中的Map<String, TableTemplate>，这个Map的value值TableTemplate
//       然后我们遍历得到entry
        for (Map.Entry<String, TableTemplate> entry :
                template.getTableTemplateMap().entrySet()) {
//          通过entry.getValue获取TableTemplate，里面包含了tableName、level、
//          还有一个Map<OpType, List<String>> OpTypeFieldSetMap，这个map的value存储的就是列名
            TableTemplate table = entry.getValue();
/**          获取map的key是update类型，即数据库更新操作对应的表的列名，即字段名
 *           ！！！注意这里是解析我们的模板json文件，我们获取的列的数量 c1，表中的列的数量c2  关系式：c1≤c2
 *           因为我们的模板json文件可以类比成dto或者vo，我们只想要监听表中的某些字段  或者  一些隐私的字段不想被监听
 *           举个例子ad_plan表含有字段：id,user_id,plan_name,plan_status,start_date,end_date,create_time,update_tiem;
 *           但是我们在json文件中监听的字段只有如下几个。。。 这里详细的写这么多注释主要是为了后面我们对代码106行的TableTemplate进行填充做铺垫
 *         {"column": "user_id"},{"column": "plan_status"},{"column": "start_date"},{"column": "end_date"}
 */
            List<String> updateFields = table.getOpTypeFieldSetMap().get(
                    OpType.UPDATE
            );
//          同上一行注释，获取add类型的列名
            List<String> insertFields = table.getOpTypeFieldSetMap().get(
                    OpType.ADD
            );
//          获取delete类型的列名
            List<String> deleteFields = table.getOpTypeFieldSetMap().get(
                    OpType.DELETE
            );
//          通过封装的jdbc，根据数据库名称database、表名tableName，按照书写的SQL_SCHEMA
//          语句，可以查询到对应的字段id和字段名
            jdbcTemplate.query(SQL_SCHEMA, new Object[]{
                    template.getDatabase(), table.getTableName()
            }, (rs, i) -> {
//              对处理的结果取出，通过mysql得到的数据库的  位置  对应的  列名
                int pos = rs.getInt("ORDINAL_POSITION");
                String colName = rs.getString("COLUMN_NAME");
//          只要不为空且只含有 模板类json文件中想要的列  然后再填充TableTemplate中的Map<Integer, String> posMap
//          posMap中的key就是binlog中的“列的位置” x ；value就是“列名”；因为binlog打印出列的位置x  等于mysql查询出的位置y-1即x=y-1
//          因此就可以按照pos-1作为key，colName作为value填入posMap；为什么位置有这个关系？详细见文档说明
                if ((null != updateFields && updateFields.contains(colName))
                        || (null != insertFields && insertFields.contains(colName))
                        || (null != deleteFields && deleteFields.contains(colName))) {
                    table.getPosMap().put(pos - 1, colName);
                }
                return null;
            });
        }
    }
}
