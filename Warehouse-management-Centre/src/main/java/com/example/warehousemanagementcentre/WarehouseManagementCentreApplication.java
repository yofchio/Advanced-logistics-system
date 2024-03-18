package com.example.warehousemanagementcentre;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.example.warehousemanagementcentre.mapper")
public class WarehouseManagementCentreApplication {

  public static void main(String[] args) {
    SpringApplication.run(WarehouseManagementCentreApplication.class, args);
  }

}
