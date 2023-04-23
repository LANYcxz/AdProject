package com.ad.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@Component
@ConfigurationProperties(prefix = "adconf.mysql")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinlogConfig {
//把application.yml文件的配置转成对应的java对象
    private String host;
    private Integer port;
    private String username;
    private String password;

    private String binlogName;
    private Long position;
}
