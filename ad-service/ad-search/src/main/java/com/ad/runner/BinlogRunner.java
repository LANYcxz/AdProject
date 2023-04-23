package com.ad.runner;

import com.ad.mysql.BinlogClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
//让应用程序启动的时候就开始监听，实现该接口
    @Slf4j
    @Component
public class BinlogRunner implements CommandLineRunner {

    private final BinlogClient client;
    @Autowired
    public BinlogRunner(BinlogClient client) {
        this.client = client;
    }
//  这样子我们的connect方法就会从整个工程一起动完成就开始connect，然后监听binlog
    @Override
    public void run(String... strings) throws Exception {
        log.info("Coming in BinlogRunner...");
        client.connect();
    }
}
