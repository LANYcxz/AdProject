package com.ad.sender.kafka;

import com.ad.mysql.dto.MySqlRowData;
import com.ad.sender.ISender;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Component("kafkaSender")
public class KafkaSender implements ISender {
    //    在配置文件中定义一个topic
    @Value("${adconf.kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sender(MySqlRowData rowData) {
        kafkaTemplate.send(
                topic,
                JSON.toJSONString(rowData)
        );
    }

    //    写一个测试方法
    @KafkaListener(topics = {"ad-search-mysql-data"}, groupId = "ad-search")
    public void processMysqlRowData(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
//            如果kafka中消息存在，就去消费
            Object message = kafkaMessage.get();
            MySqlRowData rowData = JSON.parseObject(
                    message.toString(),
                    MySqlRowData.class
            );
            System.out.println("kafka processMysqlRowData: " +
                    JSON.toJSONString(rowData));
        }
    }
}
