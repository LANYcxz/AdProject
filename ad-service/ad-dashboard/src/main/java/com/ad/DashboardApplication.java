package com.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
@EnableEurekaClient
@SpringBootApplication
@EnableHystrixDashboard
//注册到eureka当做一个微服务，交给springCloud帮我们进行监控
public class DashboardApplication {
    public static void main(String[] args) {
        SpringApplication.run(DashboardApplication.class,args);
    }
}
