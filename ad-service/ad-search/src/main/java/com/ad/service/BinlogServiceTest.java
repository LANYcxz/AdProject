package com.ad.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

import java.io.IOException;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
public class BinlogServiceTest {
    public static void main(String[] args) throws IOException {
        BinaryLogClient client=new BinaryLogClient(
                "127.0.0.1",
                3306,
                "root",
                "asd132"
        );
        client.registerEventListener(event -> {
            EventData data = event.getData();
            if (data instanceof UpdateRowsEventData) {
                System.out.println("Update--------------");
                System.out.println(data.toString());
            } else if (data instanceof WriteRowsEventData) {
                System.out.println("Write---------------");
                System.out.println(data.toString());
            } else if (data instanceof DeleteRowsEventData) {
                System.out.println("Delete--------------");
                System.out.println(data.toString());
            }
        });
        client.connect();
//        数据库查询示例代码如下：
//        insert into `ad_unit_keyword` (`unit_id`,`keyword`)VALUES (1,'雷克塞奔驰');
    }
}
