package com.yang.bi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主类（项目启动入口）
 */
// todo 如需开启 Redis，须移除 exclude 中的内容
@EnableDiscoveryClient
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("com.yang.bi.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class BIApplication {
    public static void main(String[] args) {
        SpringApplication.run(BIApplication.class, args);
    }
}
